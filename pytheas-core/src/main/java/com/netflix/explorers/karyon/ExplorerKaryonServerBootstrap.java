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
package com.netflix.explorers.karyon;

import com.google.common.collect.Lists;
import com.google.inject.Module;
import com.netflix.config.ConfigurationManager;
import com.netflix.explorers.annotations.ExplorerGuiceModule;
import com.netflix.governator.guice.LifecycleInjectorBuilder;
import com.netflix.governator.lifecycle.ClasspathScanner;
import com.netflix.karyon.server.ServerBootstrap;
import com.netflix.karyon.spi.PropertyNames;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;


/**
 * ServerBootstrap implementation for pytheas based application.
 * It scans classpath for GuiceModules marked with ExplorerGuiceModule annotation
 * and adds them to Karyon's LifecycleInjectorBuilder.
 * It also adds a JerseyServletModule exporting a list of package names containing jersey resources
 * contained in a pytheas application
 */
public class ExplorerKaryonServerBootstrap extends ServerBootstrap {
    private static final Logger LOG = LoggerFactory.getLogger(ExplorerKaryonServerBootstrap.class);

    @Override
    protected void beforeInjectorCreation(@SuppressWarnings("unused") LifecycleInjectorBuilder builderToBeUsed) {
        List<Class<? extends Annotation>> explorerGuiceAnnotations = Lists.newArrayList();
        explorerGuiceAnnotations.add(ExplorerGuiceModule.class);
        Collection<String> basePackages = getBasePackages();
        ClasspathScanner classpathScanner = new ClasspathScanner(basePackages, explorerGuiceAnnotations);

        List<Module> explorerGuiceModules = new ArrayList<Module>();

        String jerseyPkgPath = "";
        for (Class<?> explorerGuiceModuleClass : classpathScanner.getClasses()) {
            try {

                ExplorerGuiceModule guiceModAnnotation = explorerGuiceModuleClass.getAnnotation(ExplorerGuiceModule.class);
                if (guiceModAnnotation.jerseyPackagePath() != null &&
                        ! guiceModAnnotation.jerseyPackagePath().isEmpty()) {
                    jerseyPkgPath += ";" + guiceModAnnotation.jerseyPackagePath();
                }

                LOG.info("ExplorerGuiceModule init " + explorerGuiceModuleClass.getName());
                Module expGuiceModule = (Module) explorerGuiceModuleClass.newInstance();
                explorerGuiceModules.add(expGuiceModule);
            } catch (InstantiationException e) {
                LOG.error("InstantiationException in building " + explorerGuiceModuleClass.getName());
            } catch (IllegalAccessException e) {
                LOG.error("IllegalAccessException in building " + explorerGuiceModuleClass.getName());
            }
        }
        LOG.info("Total explorer guice modules added " + explorerGuiceModules.size());

        // Add containing servlet module for a pytheas app
        String appContext = ConfigurationManager.getConfigInstance().getString("com.netflix.pytheas.app.context");
        JerseyServletModule jerseyServletModule = buildJerseyServletModule(jerseyPkgPath, appContext);
        explorerGuiceModules.add(jerseyServletModule);


        builderToBeUsed.withAdditionalModules(explorerGuiceModules);
    }


    @Override
    protected Collection<String> getBasePackages() {
        List<String> toReturn = new ArrayList<String>();
        String basePackagesStr = ConfigurationManager.getConfigInstance().getString(PropertyNames.SERVER_BOOTSTRAP_BASE_PACKAGES_OVERRIDE);
        String[] basePackages = basePackagesStr.split(";");

        for (String basePackage : basePackages) {
            toReturn.add(String.valueOf(basePackage));
        }
        return toReturn;
    }

    private JerseyServletModule buildJerseyServletModule(final String jerseyPkgPath, final String servletContainerPath) {
        return new JerseyServletModule() {
            @Override
            protected void configureServlets() {
                bind(GuiceContainer.class).asEagerSingleton();
                Map<String, String> params = new HashMap<String, String>();
                params.put("com.sun.jersey.config.property.packages",
                        "com.netflix.explorers.resources;" + "com.netflix.explorers.providers;" + jerseyPkgPath);

                // Route all requests through GuiceContainer
                serve(servletContainerPath).with(GuiceContainer.class, params);
            };
        };
    }


}
