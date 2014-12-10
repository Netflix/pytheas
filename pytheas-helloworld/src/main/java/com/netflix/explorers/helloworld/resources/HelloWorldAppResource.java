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