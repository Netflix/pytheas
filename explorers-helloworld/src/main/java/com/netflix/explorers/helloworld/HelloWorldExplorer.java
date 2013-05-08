package com.netflix.explorers.helloworld;


import com.netflix.explorers.AbstractExplorerModule;
import com.netflix.explorers.ExplorerManager;
import com.netflix.karyon.spi.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Component
public class HelloWorldExplorer extends AbstractExplorerModule {

    private ExplorerManager explorerManager;

    @Inject
    public HelloWorldExplorer(ExplorerManager manager) {
        super("helloworld");
        this.explorerManager = manager;
    }

    @PostConstruct
    public void initialize() {
        super.initialize();
        explorerManager.registerExplorer(this);
    }
}
