package com.netflix.explorers.sse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;

public class OutboundEventWriter implements MessageBodyWriter<OutboundEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(OutboundEventWriter.class);

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return (type.equals(OutboundEvent.class));
    }

    @Override
    public long getSize(OutboundEvent outboundEvent, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(OutboundEvent outboundEvent, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, final OutputStream entityStream) throws IOException, WebApplicationException {
        LOG.info("HTTP headers sent " + httpHeaders.keySet());
        httpHeaders.putSingle("Access-Control-Allow-Origin","*");

    	if(outboundEvent.getComment() != null) {
            entityStream.write(String.format(": %s\n", outboundEvent.getComment()).getBytes());
        }

        if(outboundEvent.getName() != null) {
            entityStream.write(String.format("event: %s\n", outboundEvent.getName()).getBytes());
        }
        
        if(outboundEvent.getId() != null) {
            entityStream.write(String.format("id: %s\n", outboundEvent.getId()).getBytes());
        }

        String line;
        BufferedReader reader = new BufferedReader(new StringReader(outboundEvent.getData().toString()));
        try {
            while ((line = reader.readLine()) != null) {
                entityStream.write(String.format("data: %s\n", line).getBytes());
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

        entityStream.write("\n\n".getBytes());
        entityStream.flush();
    }
}
