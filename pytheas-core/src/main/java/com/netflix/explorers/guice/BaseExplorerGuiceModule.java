package com.netflix.explorers.guice;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.netflix.config.ConfigurationManager;
import com.netflix.explorers.AppConfigGlobalModelContext;
import com.netflix.explorers.ExplorerManager;
import com.netflix.explorers.ExplorersManagerImpl;
import com.netflix.explorers.annotations.ExplorerGuiceModule;
import com.netflix.explorers.context.GlobalModelContext;


@ExplorerGuiceModule
public class BaseExplorerGuiceModule extends AbstractModule {

    public static final String APP_ID = "archaius.deployment.applicationId";

    @Override
    protected void configure() {
        final String appId = ConfigurationManager.getConfigInstance().getString(APP_ID);
        bind(String.class).annotatedWith(Names.named("explorerAppName")).toInstance(appId);
        bind(GlobalModelContext.class).to(AppConfigGlobalModelContext.class);
        bind(ExplorerManager.class).to(ExplorersManagerImpl.class);
    }
}