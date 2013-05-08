package com.netflix.explorers.helloworld.resources;

import com.sun.jersey.api.view.Viewable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;


@Path("/")
public class HelloWorldAppResource {
    private Logger LOG = LoggerFactory.getLogger(HelloWorldAppResource.class);

    @GET
    @Produces( MediaType.TEXT_HTML )
    public Viewable showIndex()
    {
        LOG.info("home page");
        Map<String, Object> model = new HashMap<String, Object>();
        return new Viewable( "/helloworld/home.ftl", model );
    }
}