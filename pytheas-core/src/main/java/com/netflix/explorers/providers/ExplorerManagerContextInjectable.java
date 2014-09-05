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
package com.netflix.explorers.providers;

import java.lang.reflect.Type;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import com.netflix.explorers.ExplorerManager;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;

public class ExplorerManagerContextInjectable 
    extends    AbstractHttpContextInjectable<ExplorerManager>
    implements InjectableProvider<Context, Type> {

    private ExplorerManager manager;
    
    @Context 
    private ThreadLocal<HttpServletRequest> requestInvoker;
    
    public ExplorerManagerContextInjectable(ExplorerManager manager) {
        this.manager = manager;
    }
    
    @Override
    public Injectable<?> getInjectable(ComponentContext ic, Context a, Type c) {
        if (c.equals(ExplorerManager.class)) {
            return this;
        }
        return null;
    }

    @Override
    public ComponentScope getScope() {
        return ComponentScope.PerRequest;
    }

    @Override
    public ExplorerManager getValue(HttpContext c) {
        return manager;
    }
}
