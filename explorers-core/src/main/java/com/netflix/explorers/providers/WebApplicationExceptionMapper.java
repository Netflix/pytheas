package com.netflix.explorers.providers;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Variant;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.google.inject.Singleton;
import com.sun.jersey.api.Responses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.common.collect.Maps;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.view.Viewable;

@Singleton
@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {
    private static final Logger LOG = LoggerFactory.getLogger(GenericExceptionMapper.class);
    private static List<Variant> supportedMediaTypes = Variant.mediaTypes(MediaType.APPLICATION_JSON_TYPE, MediaType.TEXT_HTML_TYPE).add().build();
    
    @Context 
    private HttpContext context;

    public WebApplicationExceptionMapper() {
        LOG.info("GenericExceptionMapper Created");
    }
    
    @Override
    public Response toResponse(WebApplicationException error) {
        LOG.warn("WebApplicationExceptionMapper " + error.getResponse().getStatus() + " " + error.getMessage(), error);
        
        if (error.getResponse() != null && (error.getResponse().getStatus() / 100) == 3) {
            LOG.warn("Code: " + error.getResponse().getStatus());
            return error.getResponse();
        }
        
        MediaType mediaType = context.getRequest().selectVariant(supportedMediaTypes).getMediaType();
        if (mediaType != null && MediaType.APPLICATION_JSON_TYPE == mediaType) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(baos);
                error.printStackTrace(ps);

                return Response
                    .status(error.getResponse().getStatus())
                    .entity(new JSONObject()
                        .put("code",    error.getResponse().getStatus())
                        .put("url",     context.getUriInfo().getPath())
                        .put("error",   error.toString())
                        .put("message", error.getMessage())
                        .put("trace",   baos.toString())
                        )
                    .build();
            }
            catch (JSONException e) {
                // TODO:
            }
        }
        

        final int statusCode = error.getResponse().getStatus();
        if (statusCode == Responses.NOT_FOUND) {
            return Response.status(Responses.NOT_FOUND).entity(error.getMessage()).build();
        } else {
            // internal error
            Map<String, Object> model = Maps.newHashMap();
            model.put("exception", error);
            return Response.status(error.getResponse().getStatus()).entity(new Viewable("/errors/internal_error.ftl", model)).build();
        }
    }
}
