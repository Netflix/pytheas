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
        RequestContext request = new RequestContext();
        request.setHttpServletRequest(requestInvoker.get());
        try {
            return manager.getExplorer(request.getExplorerName());
        }
        catch (Exception e) {
            LOG.warn("Failed to get UserProfileDao", e);
            return null;
        }
    }
}
