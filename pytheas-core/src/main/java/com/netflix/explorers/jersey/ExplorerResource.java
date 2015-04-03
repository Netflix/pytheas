/**
 * Copyright 2014 Netflix, Inc.
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
package com.netflix.explorers.jersey;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.Provider;
import com.netflix.explorers.AbstractExplorerModule;
import com.netflix.explorers.ExplorerManager;
import com.netflix.explorers.rest.RestKey;
import com.netflix.explorers.rest.RestResource;
import com.sun.jersey.api.NotFoundException;
import com.sun.jersey.api.core.ResourceContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
/**
 * Higher level explorer module that auto creates jersey path bindings for
 * entities managed by an explorer. The ExplorerResource will be registered with
 * Jersey as a Singleton and will serve as the entry point to creating endpoints
 * for managed resources.
 * 
 * @author elandau
 * 
 */
public class ExplorerResource extends AbstractExplorerModule {
    private static final Logger LOG = LoggerFactory.getLogger(ExplorerResource.class);

    private static final String DEFAULT_CONTROLLER = "home";
    
    @Context
    private ResourceContext resourceContext;

    @Context
    private ThreadLocal<HttpServletRequest> requestInvoker;

    private final Map<String, Provider<ViewableResource>> controllers;
    private final Map<RestKey, Provider<RestResource>> restResources;

    private String defaultController = DEFAULT_CONTROLLER;

    public ExplorerResource(String name,
            Map<String, Provider<ViewableResource>> controllers,
            Map<RestKey, Provider<RestResource>> restResources) {
        super(name);
        this.controllers   = controllers;
        this.restResources = restResources;

        if (!controllers.isEmpty()) {
            LOG.info(String.format("Explorer '%s' initialized with controllers '%s'", name, this.controllers.keySet()));
            defaultController = Iterables.getFirst(controllers.keySet(), null);
        }
        
        if (restResources != null) {
            LOG.info(String.format("Explorer '%s' initialized with r '%s'", name, this.restResources.keySet()));
        }
    }

    public void initialize(ExplorerManager manager) {
        super.initialize(manager);
    }

    /**
     * Get all controllers associated with this explorer
     * 
     * @return A mapping of controller name (used as url path) and the handling
     *         class
     */
    public Map<String, Provider<ViewableResource>> getControllers() {
        return controllers;
    }

    /**
     * Get all classes that should be registered with jersey and made avaialble
     * for instatiation with dependency injection
     * @return
     */
    @Deprecated
    public List<Class<?>> getClasses() {
        return Lists.newArrayList();
    }

    /**
     * URL routing entry point for rest endpoints belonging to this explorer
     * 
     * @return
     * @throws Exception
     */
    @Path("rest/{version}/{endpoint}")
    public Object rest(@PathParam("version") String version, @PathParam("endpoint") String endpoint) throws Exception {
        if (restResources != null) {
            RestKey key = new RestKey(version, endpoint);
        
            Provider<RestResource> provider = restResources.get(key);
            
            if (provider == null) {
                throw new NotFoundException(String.format("No rest resource found for '%s:%s'", version, endpoint ));
            }      
            
            return provider.get();
        }
        throw new NotFoundException(String.format("No rest resource for explorer", getName(), endpoint));
    }

    @Path("{controller}")
    public Object controller(@PathParam("controller") String controllerName)
            throws Exception {
        Provider<ViewableResource> provider = controllers.get(controllerName);
        if (provider != null) {
            return provider.get();
        }
        return new NotFoundException(String.format(
                "Controller for '%s' not found", controllerName));
    }

    /**
     * Default implementation of the explorer home page
     * 
     * @return
     * @throws Exception
     */
    @GET
    public Response showHome() throws Exception {
        if (defaultController != null) {
            try {
                String redirect = StringUtils.join(Arrays.asList(requestInvoker.get().getPathInfo().toString(), defaultController), "/");
                return Response.temporaryRedirect(new URI(redirect)).build();
            } catch (URISyntaxException e) {
                return Response.serverError().build();
            }
        }
        return Response.ok(DEFAULT_CONTROLLER).build();
    }
}

