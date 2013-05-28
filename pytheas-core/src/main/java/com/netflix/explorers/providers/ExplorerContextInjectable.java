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
package com.netflix.explorers.providers;

import java.lang.reflect.Type;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.explorers.Explorer;
import com.netflix.explorers.ExplorerManager;
import com.netflix.explorers.context.RequestContext;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;

public class ExplorerContextInjectable 
    extends    AbstractHttpContextInjectable<Explorer>
    implements InjectableProvider<Context, Type> {
    
    private static final Logger LOG = LoggerFactory.getLogger(ExplorerContextInjectable.class);

    private ExplorerManager manager;
    
    @Context 
    private ThreadLocal<HttpServletRequest> requestInvoker;
    
    public ExplorerContextInjectable(ExplorerManager manager) {
        this.manager = manager;
    }
    
    @Override
    public Injectable<?> getInjectable(ComponentContext ic, Context a, Type c) {
        if (c.equals(Explorer.class)) {
            return this;
        }
        return null;
    }

    @Override
    public ComponentScope getScope() {
        return ComponentScope.PerRequest;
    }

    @Override
    public Explorer getValue(HttpContext c) {
        RequestContext request = manager.newRequestContext(requestInvoker.get());
        try {
            return manager.getExplorer(request.getExplorerName());
        }
        catch (Exception e) {
            LOG.warn("Failed to get UserProfileDao", e);
            return null;
        }
    }
}
