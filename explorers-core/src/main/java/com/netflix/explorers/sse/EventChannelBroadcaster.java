package com.netflix.explorers.sse;

import org.cliffc.high_scale_lib.NonBlockingHashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventChannelBroadcaster {
    private static final Logger LOG = LoggerFactory.getLogger(EventChannelBroadcaster.class);

    private NonBlockingHashSet<EventChannel> channels = new NonBlockingHashSet<EventChannel>();

    public void registerEventChannel(final EventChannel channel) {
        channels.add(channel);
        
        channel.registerListener(new EventChannelListener() {
            @Override
            public void onChannelClosing(EventChannel eventChannel) {
                channels.remove(channel);
            }
        });
    }
    
    public void write(OutboundEvent event) {
        LOG.info("write to " + channels.size() + " channels");
        for (EventChannel channel : channels) {
            try {
                channel.write(event);
            }
            catch (Throwable t) {
                channels.remove(channel);
            }
        }
    }
}
