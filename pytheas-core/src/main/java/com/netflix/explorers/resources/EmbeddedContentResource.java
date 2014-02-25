package com.netflix.explorers.resources;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Date;
import java.net.URLConnection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.netflix.config.DynamicBooleanProperty;
import com.netflix.config.DynamicIntProperty;
import com.netflix.config.DynamicPropertyFactory;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.google.common.io.ByteStreams;
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
        .put("map", "application/x-navimap")
        .put("ico", "image/x-icon")
        .put("json", MediaType.APPLICATION_JSON)
        .put("swf", "application/x-shockwave-flash")
        .build();

    private static final DynamicBooleanProperty CACHE_ENABLED = DynamicPropertyFactory.getInstance().getBooleanProperty("netflix.explorers.resources.cache.enabled", true);
    private static final DynamicIntProperty MAX_AGE = DynamicPropertyFactory.getInstance().getIntProperty("netflix.explorers.resources.cache.maxAge", 3600);

	@GET
	@Path("/{subResources:.*}")
	public Response get(@PathParam("subResources") String subResources) throws Exception {
		LOG.debug(subResources);
		
		String ext = StringUtils.substringAfterLast(subResources, ".");
		String mediaType = EXT_TO_MEDIATYPE.get(ext);
                byte[] buffer = null;

                try {
                    if (! CACHE_ENABLED.get()) {
                        final URLConnection urlConnection = getClass().getResource("/" + subResources).openConnection();
                        if (urlConnection != null) {
                            urlConnection.setUseCaches(false);
                            buffer = ByteStreams.toByteArray(urlConnection.getInputStream());
                        }
                    } else {
                        buffer = ByteStreams.toByteArray(this.getClass().getResourceAsStream("/" + subResources));
                    }
                } catch (IOException e) {
                    throw new WebApplicationException(404);
                }
        
		if (buffer == null)
			throw new NotFoundException();
		else  {
			if (CACHE_ENABLED.get()) {
				CacheControl cc = new CacheControl();
				cc.setMaxAge(MAX_AGE.get());
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
