package com.netflix.explorers.sse;

import javax.ws.rs.core.MediaType;

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
         * @param type java type of supplied data. MUST NOT be {@code null}.
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
         * <p>There are two valid configurations:
         * <ul>
         *     <li>when {@link Builder#comment} is set, all other parameters are optional. If {@link Builder#data(Class, Object)}
         *     and {@link Builder#type} is set, event will be serialized after comment.</li>
         *     <li>when {@link Builder#comment} is not set, {@link Builder#data(Class, Object)} and {@link Builder#type} HAVE TO
         *     be set, all other parameters are optional.</li>
         * </ul></p>
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
     * @param type java type of events data.
     * @param mediaType {@link MediaType} of events data.
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
