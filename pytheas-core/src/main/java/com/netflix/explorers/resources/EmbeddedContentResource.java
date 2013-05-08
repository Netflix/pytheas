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
package com.netflix.explorers.resources;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.netflix.config.ConfigurationManager;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.sun.jersey.api.NotFoundException;

@Path("/res")
public class EmbeddedContentResource {
    private static final Logger LOG = LoggerFactory.getLogger(EmbeddedContentResource.class);

	private static final ImmutableMap<String, String> EXT_TO_MEDIATYPE =
	       new ImmutableMap.Builder<String, String>()
	           .put("js",  "text/javascript")
	           .put("png", "image/png")
	           .put("gif", "image/gif")
	           .put("css", "text/css")
	           .put("jpg", "image/jpeg")
	           .put("jpeg", "image/jpeg")
	           .put("csv", "text/csv")
	           .put("ico", "image/x-icon")
	           .put("json", MediaType.APPLICATION_JSON)
               .put("swf", "application/x-shockwave-flash")
	           .build();
	
    private final static int MAX_AGE = ConfigurationManager.getConfigInstance().getInt("netflix.explorers.resources.cache.maxAge", 3600);
    private final static boolean CACHE_ENABLED = ConfigurationManager.getConfigInstance().getBoolean("netflix.explorers.resources.cache.enabled", true);

	@GET
	@Path("/{subResources:.*}")
	public Response get(@PathParam("subResources") String subResources) throws Exception {
		LOG.debug(subResources);
		
		String ext = StringUtils.substringAfterLast(subResources, ".");
		String mediaType = EXT_TO_MEDIATYPE.get(ext);
        byte[] buffer = null;
		if (mediaType != null) {
			InputStream is = getClass().getResourceAsStream("/" + subResources);
			if (is == null) {
				throw new WebApplicationException(404);
			}
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			try {
				IOUtils.copy(is, os);
				buffer = os.toByteArray();
			} catch (IOException e) {
				LOG.warn(e.getMessage());
			}
		}
        
		if (buffer == null)
			throw new NotFoundException();
		else  {
			if (CACHE_ENABLED) {
				CacheControl cc = new CacheControl();
				cc.setMaxAge(MAX_AGE);
				cc.setNoCache(false);
				return Response
				    .ok(buffer, mediaType)
				    .cacheControl(cc)
				    .expires(new Date(System.currentTimeMillis() + 3600 * 1000))
				    .tag(new String(Hex.encodeHex(MessageDigest.getInstance("MD5").digest(subResources.getBytes()))))
				    .build();
			}
			else {
				return Response.ok(buffer, mediaType).build();
			}
		}
	}
}
