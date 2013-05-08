package com.netflix.explorers.sse;

import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

//@Produces({EventChannel.SERVER_SENT_EVENTS})
//@Provider
public class EventChannelWriter implements MessageBodyWriter<EventChannel> {

    private static final Logger LOG = LoggerFactory.getLogger(EventChannel.class);

    private static final OutboundEventWriter eventWriter = new OutboundEventWriter();
    
    @Override
    public long getSize(EventChannel t, Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {
        return (type.equals(EventChannel.class));
    }

    @Override
    public void writeTo(EventChannel channel, Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, Object> httpHeaders,
            OutputStream entityStream) throws IOException,
            WebApplicationException {
        OutboundEvent event; 
        try {
            while (null != (event = channel.pop())) {
                eventWriter.writeTo(event, type, genericType, annotations, mediaType, httpHeaders, entityStream);
            }
        }
        catch (Throwable t) {
            LOG.info("Channel not writable - need to close");
            channel.close();
        }
    }
}
