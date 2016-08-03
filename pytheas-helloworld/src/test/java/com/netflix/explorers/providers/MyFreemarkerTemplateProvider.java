package com.netflix.explorers.providers;

import com.netflix.explorers.ExplorerManager;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.ext.Provider;

@Provider
@Singleton
public class MyFreemarkerTemplateProvider extends FreemarkerTemplateProvider {
    @Inject
    public MyFreemarkerTemplateProvider(ExplorerManager manager) {
        super(manager);
    }
}
