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

public class OutboundEvent {
	/**
     * Used for creating {@link OutboundEvent} instances.
     */
    public static class Builder {

        private String name;
        private String comment;
        private String id;
        private String data;

        /**
         * Set event name.
         *
         * Will be send as field name "event".
         *
         * @param name field name "event" value.
         * @return updated builder instance.
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Set event id.
         *
         * @param id event id.
         * @return updated builder instance.
         */
        public Builder id(String id) {
            this.id = id;
            return this;
        }

        /**
         * Set comment. It will be send before serialized event if it contains data or as a separate "event".
         *
         * @param comment comment string.
         * @return updated builder instance.
         */
        public Builder comment(String comment) {
            this.comment = comment;
            return this;
        }

        /**
         * Set event data and java type of event data. Type will  be used for {@link javax.ws.rs.ext.MessageBodyWriter}
         * lookup.
         *
         * @param data event data. MUST NOT be {@code null}.
         * @return updated builder instance.
         */
        public Builder data(String data) {
            if(data == null) {
                throw new IllegalArgumentException();
            }

            this.data = data;
            return this;
        }

        /**
         * Build {@link OutboundEvent}.
         *
         * There are two valid configurations:
         * <ul>
         *     <li>when {@link Builder#comment} is set, all other parameters are optional. If {@link Builder#data(String)}
         *     is set, event will be serialized after comment.</li>
         *     <li>when {@link Builder#comment} is not set, {@link Builder#data(String)} HAVE TO
         *     be set, all other parameters are optional.</li>
         * </ul>
         *
         * @return new {@link OutboundEvent} instance.
         * @throws IllegalStateException when called with invalid configuration.
         */
        public OutboundEvent build() throws IllegalStateException {
            if(comment == null) {
                if(data == null) {
                    throw new IllegalStateException();
                }
            }

            return new OutboundEvent(name, id, data, comment);
        }
    }

    private final String name;
    private final String comment;
    private final String id;
    private final String data;

    /**
     * Create new OutboundEvent with given properties.
     *
     * @param name event name (field name "event").
     * @param id event id.
     * @param data events data.
     * @param comment comment.
     */
    public OutboundEvent(String name, String id, String data, String comment) {
        this.name = name;
        this.comment = comment;
        this.id = id;
        this.data = data;
    }

    /**
     * Get event name.
     *
     * @return event name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get event id.
     *
     * @return event id.
     */
    public String getId() {
        return id;
    }

    /**
     * Get comment
     *
     * @return comment.
     */
    public String getComment() {
        return comment;
    }

    /**
     * Get event data.
     *
     * @return event data.
     */
    public String getData() {
        return data;
    }
}
