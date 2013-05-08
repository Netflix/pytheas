/*
 * Copyright 2013 Netflix, Inc.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package com.netflix.explorers.helloworld;

import com.google.inject.name.Names;
import com.netflix.explorers.AppConfigGlobalModelContext;
import com.netflix.explorers.ExplorerManager;
import com.netflix.explorers.ExplorersManagerImpl;
import com.netflix.explorers.context.GlobalModelContext;
import com.netflix.governator.guice.LifecycleInjectorBuilder;
import com.netflix.karyon.server.ServerBootstrap;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class HelloWorldBootstrap extends ServerBootstrap {
    private static final Logger LOG = LoggerFactory.getLogger(HelloWorldBootstrap.class);

    @Override
    protected void beforeInjectorCreation(@SuppressWarnings("unused") LifecycleInjectorBuilder builderToBeUsed) {

       JerseyServletModule jerseyServletModule = new JerseyServletModule() {
            @Override
            protected void configureServlets() {
                bind(String.class).annotatedWith(Names.named("explorerAppName")).toInstance("helloworld");
                bind(GlobalModelContext.class).to(AppConfigGlobalModelContext.class);
                bind(ExplorerManager.class).to(ExplorersManagerImpl.class);
                bind(HelloWorldExplorer.class);

                bind(GuiceContainer.class).asEagerSingleton();

                Map<String, String> params = new HashMap<String, String>();
                params.put("com.sun.jersey.config.property.packages",
                        "com.netflix.explorers.resources;" +
                        "com.netflix.explorers.providers;" +
                        "com.netflix.explorers.helloworld.resources");

                // Route all requests through GuiceContainer
                serve("/*").with(GuiceContainer.class, params);
            }
        };
        builderToBeUsed.withAdditionalModules(jerseyServletModule);
        LOG.debug("HelloWorldBootstrap injected jerseyServletModule in LifecycleInjectorBuilder");
    }
}
