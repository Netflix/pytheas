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
package com.netflix.explorers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.common.collect.Lists;
import com.netflix.explorers.context.RequestContext;
import freemarker.cache.TemplateLoader;
import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Supplier;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.netflix.explorers.context.GlobalModelContext;
import com.netflix.explorers.services.ExplorerServiceCachedFactorySupplier;
import com.netflix.explorers.services.ExplorerServiceInstanceSupplier;

import javax.servlet.http.HttpServletRequest;

@Singleton
public class ExplorersManagerImpl implements ExplorerManager {
    private static final Logger LOG = LoggerFactory.getLogger(ExplorersManagerImpl.class);

    private final ConcurrentMap<String, Explorer> explorers = Maps.newConcurrentMap();
    private final ConcurrentMap<Class<?>, Supplier<?>> services = Maps.newConcurrentMap();
    private final GlobalModelContext globalContext;

    @Inject
    public ExplorersManagerImpl(GlobalModelContext globalContext) {
        this.globalContext = globalContext;
    }

    @Override
    public synchronized void initialize() {

    }

    @Override
    public synchronized void shutdown() {
    }

    @Override
    public synchronized void registerExplorersFromClassNames(Set<String> classNames) {
    }

    @Override
    public synchronized void registerExplorerFromClassName(String className) throws Exception {
    }

    @Override
    public synchronized void registerExplorer(Explorer explorer) {
        if (explorers.containsKey(explorer.getName())) {
            throw new RuntimeException("Already exists");
        }
        explorers.put(explorer.getName(), explorer);
    }

    public Collection<Explorer> getExplorers() {
        ArrayList<Explorer> modules = new ArrayList<Explorer>(this.explorers.values());
        Collections.sort(modules, new Comparator<Explorer>() {
            @Override
            public int compare(Explorer arg0, Explorer arg1) {
                return String.valueOf(arg0.getTitle()).compareToIgnoreCase(String.valueOf(arg1.getTitle()));
            }
        });
        return modules;
    }

    @Override
    public Explorer getExplorer(String name) {
        return explorers.get(name);
    }

    @Override
    public String getDefaultModule() {
        String defaultExplorer = globalContext.getDefaultExplorerName();
        if (String.valueOf(defaultExplorer) == "") {
            if (!explorers.isEmpty())
                defaultExplorer = explorers.keySet().iterator().next();
        }
        return defaultExplorer;
    }

    @Override
    public GlobalModelContext getGlobalModel() {
        return globalContext;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getService(Class<T> className) {
        Supplier<?> supplier = services.get(className);
        if (supplier == null)
            return null;
        return (T)supplier.get();
    }

    @Override
    public <T> void registerService(Class<T> serviceClass, T instance) {
        registerService(serviceClass, new ExplorerServiceInstanceSupplier<T>(instance));
    }

    @Override
    public <T> void registerService(Class<T> serviceClass, Supplier<T> supplier) {
        if (null != services.putIfAbsent(serviceClass, supplier)) {
            throw new RuntimeException("Service for " + serviceClass.getCanonicalName() + " already registered");
        }
    }

    @Override
    public <T> void registerService(Class<T> serviceClass, Class<? extends T> serviceImplClassName) {
        registerService(serviceClass, new ExplorerServiceCachedFactorySupplier<T>(serviceImplClassName));
    }

    @Override
    public Configuration getConfiguration() {
        return  null;
    }

    @Override
    public boolean getHasAuthProvider() {
        return false;
    }

    @Override
    public String toString() {
        return "ExplorersManagerImpl [explorers=" + explorers + ", ExplorerGlobalContext="
                + globalContext + "]";
    }

    @Override
    public void unregisterExplorer(Explorer module) {
        LOG.info("Removing explorer module " + module.getName());
        explorers.remove(module);
    }

    @Override
    public List<TemplateLoader> getAdditionalTemplateLoaders() {
        return Lists.newArrayList();
    }

    @Override
    public RequestContext newRequestContext(HttpServletRequest httpServletRequest) {
        RequestContext request = new RequestContext();
        request.setHttpServletRequest(httpServletRequest);
        return request;
    }
}
