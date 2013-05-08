package com.netflix.explorers.jersey;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.netflix.explorers.AbstractExplorerModule;
import com.netflix.explorers.ExplorerManager;
import com.netflix.explorers.annotations.Controller;
import com.sun.jersey.api.NotFoundException;
import com.sun.jersey.api.core.ResourceContext;

/**
 * Higher level explorer module that auto creates jersey path bindings for entities managed
 * by an explorer.  The ExplorerResource will be registered with Jersey as a Singleton and
 * will serve as the entry point to creating endpoints for managed resources.
 * 
 * @author elandau
 *
 */
public class ExplorerResource extends AbstractExplorerModule {
    private static final Logger LOG = LoggerFactory.getLogger(ExplorerResource.class);
    
    @Context
    private ResourceContext resourceContext;
    
    @Context
    private UriInfo uriInfo;

    @Context 
    private ThreadLocal<HttpServletRequest> requestInvoker;

    private final Map<String, Class<?>>      controllers   = Maps.newHashMap();
    private final List<ExplorerRestResource> restResources = Lists.newArrayList();
    private String defaultController = "home";
    
    public ExplorerResource(String name) {
        super(name);
    }
    
    public void initialize(ExplorerManager manager) {
        super.initialize(manager);
    }
    
    public void setDefaultController(String controller) {
        this.defaultController = controller;
    }
    
    public void setDefaultController(Class<?> controller) {
        Controller annotation = controller.getAnnotation(Controller.class);
        if (annotation == null)
            throw new RuntimeException(controller.getCanonicalName() + " must be annotated with " + Controller.class.getCanonicalName());
        
        defaultController = annotation.value();
    }
    
    /**
     * Get all controllers associated with this explorer
     * @return  A mapping of controller name (used as url path) and the handling class
     */
    public Map<String, Class<?>> getControllers() {
        return controllers;
    }
    
    public void addController(Class<?> controller) {
        Controller annotation = controller.getAnnotation(Controller.class);
        if (annotation == null)
            throw new RuntimeException(controller.getCanonicalName() + " must be annotated with " + Controller.class.getCanonicalName());
        
        controllers.put(annotation.value(), controller);
    }
    
    /**
     * Get the rest endpoints associated with this explorer.
     * @return
     */
    public List<ExplorerRestResource> getRestEndpoints() {
        return restResources;
    }
    
    /**
     * Get all classes that should be registered with jersey and made avaialble
     * for instatiation with dependency injection
     * @return
     */
    public List<Class<?>> getClasses() {
        List<Class<?>> classes = Lists.newArrayList();
        classes.addAll(getControllers().values());
        
        for (ExplorerRestResource rest : getRestEndpoints()) {
            classes.addAll(rest.getEndpoints().values());
        }

        return classes;
    }
    
    /**
     * URL routing entry point for rest endpoints belonging to this explorer
     * @return
     * @throws Exception
     */
    @Path("rest/{version}/{endpoint}")
    public Object rest(@PathParam("version") String version, @PathParam("endpoint") String endpoint) throws Exception {
        for (ExplorerRestResource v : restResources) {
            if (v.getVersion().equals(version)) {
                Class<?> res = v.getEndpoint(endpoint);
                if (res == null)
                    throw new NotFoundException();
                
                return resourceContext.getResource(res);
            }
        }
        
        return new NotFoundException();
    }
    
    @Path("{controller}") 
    public Object controller(@PathParam("controller") String controllerName) throws Exception {
        Class<?> c = controllers.get(controllerName);
        if (c != null) {
            return resourceContext.getResource(c);
        }
        return new NotFoundException();
    }
    
    /**
     * Default implementation of the explorer home page
     * @return
     * @throws Exception
     */
    @GET
    public Response home() throws Exception {
        if (defaultController != null) {
            try {
                String redirect = StringUtils.join(
                        Arrays.asList(uriInfo.getRequestUri().toString(),
                                      defaultController), 
                                      "/");
                return Response.temporaryRedirect(new URI(redirect)).build();
            } catch (URISyntaxException e) {
                return Response.serverError().build();
            }
        }
        return Response.ok("home").build();
    }
}
