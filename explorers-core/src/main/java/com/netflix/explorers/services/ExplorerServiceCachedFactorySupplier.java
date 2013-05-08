package com.netflix.explorers.services;

import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Supplier;

public class ExplorerServiceCachedFactorySupplier<T> implements Supplier<T> {
    private static final Logger LOG = LoggerFactory.getLogger(ExplorerServiceCachedFactorySupplier.class);
    
    private Class<? extends T> implementationClass;
    private AtomicReference<T> ref;
    
    public  ExplorerServiceCachedFactorySupplier(Class<? extends T> implementationClass) {
        this.implementationClass = implementationClass;
        ref = new AtomicReference<T>();
    }
    
    @Override
    public T get() {
        T instance = ref.get();
        if (instance == null) {
            try {
                instance = implementationClass.newInstance();
            }
            catch (Throwable t) {
                LOG.error("Error instantiating " + implementationClass.getCanonicalName(), t);
                throw new RuntimeException(t);
            }
            if (ref.compareAndSet(null, instance)) {
                return instance;
            }
            return ref.get();
        }
        return instance;
    }
}
