/**
 * Copyright 2013 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netflix.explorers.providers;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.netflix.explorers.Explorer;
import com.netflix.explorers.ExplorerManager;
import com.netflix.explorers.context.GlobalModelContext;
import com.netflix.explorers.context.RequestContext;
import com.netflix.explorers.model.EmptyExplorer;
import com.sun.jersey.api.view.Viewable;
import freemarker.cache.*;
import freemarker.template.Configuration;
import freemarker.template.TemplateModelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLStreamHandler;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
@Provider
public class FreemarkerTemplateProvider implements MessageBodyWriter<Viewable>
{
    private static final Logger LOG = LoggerFactory.getLogger(FreemarkerTemplateProvider.class);
    private static Map<String, URLStreamHandler> urlHandlers = Maps.newConcurrentMap();
    
    public static void addUrlHandler(String protocol, URLStreamHandler handler) {
        urlHandlers.put(protocol, handler);
    }
    
    private static final String ROOT_PATH = "/WEB-INF/templates";
    private static final String DEFAULT_LAYOUT = "main";
    
    private Configuration fmConfig = new Configuration();
    
    private ExplorerManager manager;


    @Context 
    private ThreadLocal<HttpServletRequest> requestInvoker;


    @Inject
    public FreemarkerTemplateProvider(ExplorerManager manager) {
        this.manager = manager;

        if (this.manager == null) {
            throw new RuntimeException("Manager is null");
        }

        LOG.info(this.manager.toString());

        commonConstruct();
    }
    
    private void commonConstruct() {
        // Just look for files in the class path
        fmConfig.setTemplateLoader( 
                new MultiTemplateLoader(
                        new TemplateLoader[]{ 
                                new ClassTemplateLoader( getClass(), "/")}) );
        fmConfig.setNumberFormat( "0" );
        fmConfig.setLocalizedLookup( false );
        fmConfig.setTemplateUpdateDelay(0);
        
        try {
            fmConfig.setSharedVariable("Global",    manager.getGlobalModel());
            fmConfig.setSharedVariable("Explorers", manager);
            fmConfig.setSharedVariable("toJson",    new ToJsonMethod());
        }
        catch (TemplateModelException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long getSize(Viewable t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        if (   !(   mediaType.isCompatible(MediaType.TEXT_HTML_TYPE) 
                 || mediaType.isCompatible(MediaType.APPLICATION_XHTML_XML_TYPE)) 
            || !Viewable.class.isAssignableFrom(type)) {
            return false;
        }
        
        return true;
    }

    /**
     * Write the HTML by invoking the FTL template
     * 
     * Variables accessibile to the template
     * 
     * it         - The 'model' provided by the controller
     * Explorer   - IExplorerModule reference
     * Explorers  - Map of all explorer modules
     * Global     - Global variables from the ExploreModule manager
     * Request    - The HTTPRequestHandler
     * Instance   - Information about the running instance
     * Headers    - HTTP headers
     * Parameters - HTTP parameters
     */
    @SuppressWarnings( { "unchecked" } )
    @Override
    public void writeTo(Viewable viewable, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, Object> httpHeaders, OutputStream out) throws IOException,
            WebApplicationException {
        
        String resolvedPath = viewable.getTemplateName();
        Object model = viewable.getModel();
        
        LOG.debug( "Evaluating freemarker template (" + resolvedPath + ") with model of type " +
                ( model == null ? "null" : model.getClass().getSimpleName() ) );


        // Build the model context that will be passed to the page
        final Map<String,Object> vars;
        if ( model instanceof Map ) {
            vars = new HashMap<String, Object>( (Map<String, Object>)model );
        }
        else {
            vars = new HashMap<String, Object>();
            vars.put("it", model);
        }

        RequestContext requestContext = manager.newRequestContext(requestInvoker != null ? requestInvoker.get() : null);
        vars.put("RequestContext",  requestContext);
        vars.put("Request",         requestInvoker != null ? requestInvoker.get() : null);
        
        Principal ctx = null;
        if (requestInvoker.get() != null) {
            ctx = requestInvoker.get().getUserPrincipal();
            if (ctx == null && requestInvoker.get().getSession(false) != null) {
                final String username = (String) requestInvoker.get().getSession().getAttribute("SSO_UserName");
                if (username != null) {
                    ctx = new Principal() {
                        @Override
                        public String getName() {
                            return username;
                        }
                    };
                }
            }
        }
        vars.put("Principal",  ctx);
        
        // The following are here for backward compatibility and should be deprecated as soon as possible
        Map<String, Object> global = Maps.newHashMap();
        
        GlobalModelContext globalModel = manager.getGlobalModel();

        global.put("sysenv",    globalModel.getEnvironment());      // TODO: DEPRECATE
        vars.put("global",      global);                            // TODO: DEPRECATE
        vars.put("pathToRoot",  requestContext.getPathToRoot());    // TODO: DEPRECATE
        
        String layout = DEFAULT_LAYOUT;

        Explorer explorer = null;
        final boolean hasExplorerName = !requestContext.getExplorerName().isEmpty();
        try {
            if (hasExplorerName) {
                explorer = manager.getExplorer(requestContext.getExplorerName());
            }
        }
        catch (Exception e) {
            LOG.warn(e.getMessage());
        }
        if (explorer == null) {
            if (hasExplorerName) {
                throw new WebApplicationException(new RuntimeException("Invalid explorer"), Response.Status.NOT_FOUND);
            } else {
                explorer = EmptyExplorer.getInstance();
            }
        }

        layout = explorer.getLayoutName();
        vars.put("Explorer", explorer);
        
        if (vars.containsKey("layout")) {
            layout = (String) vars.get("layout");
        }

        out.flush(); // send status + headers

        final OutputStreamWriter writer = new OutputStreamWriter( out );
        try {
            if (requestContext.getIsAjaxRequest()) {
    	        fmConfig.getTemplate(resolvedPath).process(vars, writer);
            }
            else {
    	        if (layout == null) {
    	        	layout = DEFAULT_LAYOUT;
    			}
    	    	vars.put("nestedpage", resolvedPath);
    	    	
                fmConfig.getTemplate(requestContext.getMainTemplatePath(layout)).process(vars, writer);
            }
    	
            if ( LOG.isDebugEnabled() )
                LOG.debug( "OK: Resolved freemarker template" );
        }
        catch ( Throwable t ) {
            LOG.error( "Error processing freemarker template @ " + resolvedPath + ": " + t.getMessage(), t );
            out.write( "<pre>".getBytes() );
            t.printStackTrace( new PrintStream( out ) );
            out.write( "</pre>".getBytes() );
        }
    }

    @Context
    public void setServletContext( final ServletContext context )
    {
        List<TemplateLoader> templateLoaders = Lists.newArrayList(
                new WebappTemplateLoader( context, ROOT_PATH ),
                new ClassTemplateLoader( getClass(), "/"),
                new URLTemplateLoader() {
                    @Override
                    protected URL getURL(String url) {
                        // Load from URL.
                        try {
                            String split[] = url.split(":", 2);
                            return new URL(null, url, urlHandlers.get(split[0]));
                        } catch (Exception x) {
                            LOG.error("Unable to handle url=" + url, x);
                            return null;
                        }
                    }
                    // Force reload each time.
                    public long getLastModified(Object templateSource) {
                        // TOOO: keep a running time delay to allow for some caching.
                        return System.currentTimeMillis();
                    }
                }
        );
        templateLoaders.addAll(manager.getAdditionalTemplateLoaders());

        fmConfig.setTemplateLoader(new MultiTemplateLoader(
                templateLoaders.toArray(new TemplateLoader[templateLoaders.size()])));
        
        fmConfig.setNumberFormat( "0" );
        fmConfig.setLocalizedLookup( false );
        fmConfig.setTemplateUpdateDelay(0);
        
        LOG.info(manager.toString());
        try {
            fmConfig.setSharedVariable("Global",    manager.getGlobalModel());
            fmConfig.setSharedVariable("Explorers", manager);
            fmConfig.setSharedVariable("toJson",    new ToJsonMethod());
            fmConfig.addAutoInclude("/layout/bootstrap/form.ftl");
        }
        catch (TemplateModelException e) {
            throw new RuntimeException(e);
        }
    }
    
}
