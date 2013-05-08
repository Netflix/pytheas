package com.netflix.explorers.sse;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import javax.ws.rs.core.MediaType;

import com.netflix.explorers.resources.EmbeddedContentResource;
import org.cliffc.high_scale_lib.NonBlockingHashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.util.LocaleServiceProviderPool;

public class EventChannel implements Closeable {
    private static final Logger LOG = LoggerFactory.getLogger(EventChannel.class);
    /**
     * {@link String} representation of Server sent events media type. ("{@value}").
     */
    public static final String SERVER_SENT_EVENTS = "text/event-stream";

    /**
     * Server sent events media type.
     */
    public static final MediaType SERVER_SENT_EVENTS_TYPE = MediaType.valueOf(SERVER_SENT_EVENTS);

    private final static OutboundEvent shutdownEvent = new OutboundEvent(null, null, null, null);
    
    private final BlockingDeque<OutboundEvent> queue = new LinkedBlockingDeque<OutboundEvent>();
    
    private boolean closed = false;
    
    private NonBlockingHashSet<EventChannelListener> listeners = new NonBlockingHashSet<EventChannelListener>();
    
    public EventChannel() {
        
    }
    
    public void registerListener(EventChannelListener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(EventChannelListener listener) {
        listeners.remove(listener);
    }
    
    public void notifyClosing() {
        for (EventChannelListener listener : listeners) {
            listener.onChannelClosing(this);
        }
    }
    
    public void write(OutboundEvent event) {
        if (closed == false)
            queue.add(event);
    }
    
    /**
     * Return the next event or null if closed
     * @return
     */
    public OutboundEvent pop() {
        OutboundEvent event;
        try {
            event = queue.take();
            if (closed || event == shutdownEvent)
                return null;
            if (closed) 
                return null;
            return event;
        } catch (InterruptedException e) {
            return null;
        }
    }   

    @Override
    public void close() throws IOException {
        LOG.info("Closing event channel");
        closed = true;
        queue.add(shutdownEvent);
        notifyClosing();
    }
    
    public boolean isClosed() {
        return closed;
    }
    
    
}
