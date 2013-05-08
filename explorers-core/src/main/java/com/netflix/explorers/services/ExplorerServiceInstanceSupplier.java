package com.netflix.explorers.services;

import com.google.common.base.Supplier;

public class ExplorerServiceInstanceSupplier<T> implements Supplier<T> {
    private final T instance;
    
    public ExplorerServiceInstanceSupplier(T instance) {
        this.instance = instance;
    }

    @Override
    public T get() {
        return instance;
    }
}
