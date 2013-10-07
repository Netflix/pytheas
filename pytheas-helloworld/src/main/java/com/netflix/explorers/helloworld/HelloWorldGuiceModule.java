package com.netflix.explorers.helloworld;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.netflix.explorers.Explorer;
import com.netflix.explorers.annotations.ExplorerGuiceModule;

@ExplorerGuiceModule(jerseyPackagePath = "com.netflix.explorers.helloworld.resources")
public class HelloWorldGuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<Explorer> explorersBinder = Multibinder.newSetBinder(binder(), Explorer.class);
        explorersBinder.addBinding().to(HelloWorldExplorer.class);
    }
}
