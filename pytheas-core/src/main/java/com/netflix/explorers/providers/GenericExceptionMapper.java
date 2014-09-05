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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Variant;
import javax.ws.rs.ext.ExceptionMapper;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.view.Viewable;

public class GenericExceptionMapper implements ExceptionMapper<Exception> {
    private static final Logger LOG = LoggerFactory.getLogger( GenericExceptionMapper.class );
    private static List<Variant> supportedMediaTypes = Variant.mediaTypes(MediaType.APPLICATION_JSON_TYPE, MediaType.TEXT_HTML_TYPE).add().build();
    
    @Context 
    private HttpContext context;

    public GenericExceptionMapper() {
        LOG.info("GenericExceptionMapper Created");
    }
    
    @Override
    public Response toResponse(Exception error) {
        LOG.warn("GenericExceptionMapper", error);
        
        MediaType mediaType = context.getRequest().selectVariant(supportedMediaTypes).getMediaType();
        if (mediaType != null && MediaType.APPLICATION_JSON_TYPE == mediaType) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(baos);
                error.printStackTrace(ps);
                return Response
                    .status(Status.INTERNAL_SERVER_ERROR)
                    .entity(new JSONObject()
                        .put("code",    500)
                        .put("url",     context.getUriInfo().getPath())
                        .put("error",   error.toString())
                        .put("message", error.getMessage())
                        .put("trace",   baos.toString()))
                    .build();
            }
            catch (JSONException e) {
                LOG.warn("Exception processing JSON ", e);
            }
        }
        Map<String, Object> model = Maps.newHashMap();
        model.put("exception", error);

        return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new Viewable("/errors/internal_error.ftl", model)).build();
    }
}
