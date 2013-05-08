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
package com.netflix.explorers.jersey;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.netflix.explorers.Explorer;
import com.netflix.explorers.annotations.Controller;
import com.sun.jersey.api.view.Viewable;

@Produces( MediaType.TEXT_HTML )
public class ViewableResource {
    private static final Logger LOG = LoggerFactory.getLogger(ViewableResource.class);
    private static final String DEFAULT_ACTION = "index";
    
    private @Inject UriInfo uriInfo;
    
    private String name;
    
    private String defaultAction;
    
    private Explorer explorer;
    
    public ViewableResource(Explorer explorer) {
        this.name = getName();
        this.defaultAction = DEFAULT_ACTION;
        this.explorer = explorer;
    }
    
    @GET
    public Response defaultPage() throws Exception {
        return this.redirect(defaultAction);
    }

    public Viewable view(String page) {
        return view(page, new HashMap<String, Object>());
    }
    
    public Viewable view(String page, Map<String, Object> model) {
        return new Viewable( "/" + explorer.getName() + "/" + name + "/" + page + ".ftl", model );
    }
    
    public Response redirect(String resource) {
        try {
            String redirect = StringUtils.join(
                    Arrays.asList(uriInfo.getRequestUri().toString(),
                                  resource), 
                                  "/");
            
            return Response.temporaryRedirect(new URI(redirect)).build();
        } catch (URISyntaxException e) {
            return Response.serverError().build();
        }
    }
    
    public String getName() {
        if (this.name == null) {
            Controller controller = this.getClass().getAnnotation(Controller.class);
            if (controller != null) {
                return controller.value();
            }
        }
        return this.name;
    }

}
