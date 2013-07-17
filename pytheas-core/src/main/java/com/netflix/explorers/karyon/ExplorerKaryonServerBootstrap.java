package com.netflix.explorers.karyon;

import com.google.common.collect.Lists;
import com.google.inject.Module;
import com.netflix.explorers.annotations.ExplorerGuiceModule;
import com.netflix.governator.guice.LifecycleInjectorBuilder;
import com.netflix.governator.lifecycle.ClasspathScanner;
import com.netflix.karyon.server.ServerBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;


public class ExplorerKaryonServerBootstrap extends ServerBootstrap {
    private static final Logger LOG = LoggerFactory.getLogger(ExplorerKaryonServerBootstrap.class);

    @Override
    protected void beforeInjectorCreation(@SuppressWarnings("unused") LifecycleInjectorBuilder builderToBeUsed) {
        List<Class<? extends Annotation>> explorerGuiceAnnotations = Lists.newArrayList();
        explorerGuiceAnnotations.add(ExplorerGuiceModule.class);
        ClasspathScanner classpathScanner = new ClasspathScanner(getBasePackages(), explorerGuiceAnnotations);

        List<Module> explorerGuiceModules = new ArrayList<Module>();

        for (Class<?> explorerGuiceModuleClass : classpathScanner.getClasses()) {
            try {
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

        builderToBeUsed.withAdditionalModules(explorerGuiceModules);
    }
}
