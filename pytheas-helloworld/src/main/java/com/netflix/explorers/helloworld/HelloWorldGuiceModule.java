package com.netflix.explorers.helloworld;

import com.google.inject.AbstractModule;
import com.netflix.explorers.annotations.ExplorerGuiceModule;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import java.util.HashMap;
import java.util.Map;

@ExplorerGuiceModule(jerseyPackagePath = "com.netflix.explorers.helloworld.resources")
public class HelloWorldGuiceModule extends AbstractModule {
    @Override
    protected void configure() {
    }
}
