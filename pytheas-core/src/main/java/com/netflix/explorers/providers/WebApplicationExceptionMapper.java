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
package com.netflix.explorers.providers;

import com.google.inject.Singleton;
import com.sun.jersey.api.core.HttpContext;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Variant;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

@Singleton
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {
    private static final Logger LOG = LoggerFactory.getLogger(WebApplicationExceptionMapper.class);
    private static List<Variant> supportedMediaTypes = Variant.mediaTypes(MediaType.APPLICATION_JSON_TYPE, MediaType.TEXT_HTML_TYPE).add().build();
    
    @Context 
    private HttpContext context;

    public WebApplicationExceptionMapper() {
        LOG.info("GenericExceptionMapper Created");
    }
    
    @Override
    public Response toResponse(WebApplicationException error) {
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
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(new JSONObject()
                                    .put("code", error.getResponse().getStatus())
                                    .put("url", context.getUriInfo().getPath())
                                    .put("error", error.toString())
                                    .put("message", error.getMessage())
                                    .put("trace", baos.toString())
                    )
                    .build();
            }
            catch (JSONException e) {
                // TODO:
            }
        }

        // do not log 404
        if (! is404(error)) {
            LOG.warn(String.format("WebApplicationExceptionMapper status='%s' message='%s' url='%s'",
                    error.getResponse().getStatus(), error.getMessage(), context.getRequest().getPath()),
                    error);
        }


        return Response.status(error.getResponse().getStatus()).build();
    }

    private boolean is404(WebApplicationException error) {
        return error.getResponse().getStatus() == Response.Status.NOT_FOUND.getStatusCode();
    }
}
