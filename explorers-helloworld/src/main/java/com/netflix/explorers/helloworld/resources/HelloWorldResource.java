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
package com.netflix.explorers.helloworld.resources;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.view.Viewable;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


@Path("/helloworld")
public class HelloWorldResource {
    private Logger LOG = LoggerFactory.getLogger(HelloWorldResource.class);

    @GET
    @Produces( MediaType.TEXT_HTML )
    public Viewable showIndex()
    {
        LOG.info("showIndex");
        Map<String, Object> model = new HashMap<String, Object>();
        return new Viewable( "/helloworld/index.ftl", model );
    }


    @Produces({"application/json"})
    @Path("/list")
    @GET
    public Response getCountryList() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream resourceAsStream = classLoader.getResourceAsStream("country-list");
        BufferedReader br = new BufferedReader(new InputStreamReader(resourceAsStream));

        JSONObject output = new JSONObject();
        String line = null;
        try {
            JSONArray countryList = new JSONArray();

            while ((line = br.readLine()) != null) {
                int firstSpaceCharIndex = line.indexOf(" ");
                final String code = line.substring(0, firstSpaceCharIndex);
                final String countryName = line.substring(firstSpaceCharIndex + 1);
                if (code != null && ! code.isEmpty() &&
                        countryName != null && ! countryName.isEmpty()) {
                    JSONObject countryObj = new JSONObject();
                    countryObj.put("code", code);
                    countryObj.put("name", countryName);

                    countryList.put(countryObj);
                }
            }
            output.put("countries", countryList);
        } catch (IOException e) {
            LOG.error("IOException in reading country list", e);
        } catch (JSONException e) {
            LOG.error("JSONException in building country list ", e);
        }

        return Response.ok(output.toString()).build();
    }
}
