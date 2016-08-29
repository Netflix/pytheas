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
package com.netflix.explorers.sse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class EventChannelBroadcaster {
    private static final Logger LOG = LoggerFactory.getLogger(EventChannelBroadcaster.class);

    private Set<EventChannel> channels = Collections.newSetFromMap(new ConcurrentHashMap<EventChannel, Boolean>());

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
