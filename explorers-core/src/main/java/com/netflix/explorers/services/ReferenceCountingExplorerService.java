package com.netflix.explorers.services;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class ReferenceCountingExplorerService implements ExplorerService {

    protected abstract void initialize();
    
    protected abstract void shutdown();
    
    private final AtomicInteger initCounter = new AtomicInteger();
    
    @Override
    public final void start() {
        if (initCounter.getAndIncrement() == 0) {
            initialize();
        }
    }

    @Override
    public final void stop() {
        if (initCounter.getAndDecrement() == 0) {
            shutdown();
        }
    }

}
