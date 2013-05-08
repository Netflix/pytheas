package com.netflix.explorers.jersey;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.explorers.Explorer;
import com.netflix.explorers.annotations.Controller;
import com.sun.jersey.api.view.Viewable;

@Produces( MediaType.TEXT_HTML )
public class ViewableResource {
    private static final Logger LOG = LoggerFactory.getLogger(ViewableResource.class);
    private static final String DEFAULT_ACTION = "index";
    
    @Context
    private Explorer    explorer;
    
    @Context
    private UriInfo uriInfo;
    
    private String name;
    
    private String defaultAction;
    
    public ViewableResource() {
        this.name = getName();
        this.defaultAction = DEFAULT_ACTION;
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
