package com.netflix.explorers.jersey;

import java.util.Map;

import com.google.common.collect.Maps;

public class ExplorerRestResource {
    private final Map<String, Class<?>> endpoints = Maps.newHashMap();
    private final String version;
    
    public ExplorerRestResource(String version) {
        this.version = version;
    }
    
    public ExplorerRestResource addEndpoint(String name, Class<?> resource) {
        endpoints.put(name, resource);
        return this;
    }
    
    public Class<?> getEndpoint(String endpoint) {
        return endpoints.get(endpoint);
    }
    
    public Map<String, Class<?>> getEndpoints() {
        return endpoints;
    }

    public String getVersion() {
        return this.version;
    }
}
