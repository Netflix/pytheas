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
