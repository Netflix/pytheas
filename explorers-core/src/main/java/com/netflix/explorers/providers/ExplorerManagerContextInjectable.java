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
