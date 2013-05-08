/**
 * Copyright 2013 Netflix, Inc.
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
package com.netflix.explorers;

import java.util.Collection;
import java.util.Set;

import org.apache.commons.configuration.Configuration;

import com.google.common.base.Supplier;
import com.google.inject.ImplementedBy;
import com.netflix.explorers.context.GlobalModelContext;

/**
 * Manages all explorer modules
 * @author elandau
 *
 */
//@ImplementedBy(AbstractExplorerManager.class)
public interface ExplorerManager {
    /**
     * Initialize the module manager
     */
    void initialize();
    
    /**
     * Shut down the module manager
     */
    void shutdown();
    
	/** 
	 * Return the default module to show when no other module is selected
	 * @return
	 */
	String getDefaultModule();
	
	/**
	 * Add a module to the list of modules
	 * @param def
	 */
	void registerExplorer(Explorer module);

    /**
     * Remove a previously registered explorer
     * @param remoteExplorerModule
     */
    void unregisterExplorer(Explorer module);
	
	/**
	 * Register an explorer at runtime by giving a class name
	 * @param className
	 * @throws Exception
	 */
	void registerExplorerFromClassName(String className) throws Exception;
	
	/**
	 * Return the module for this name
	 * @param name
	 * @return
	 */
	Explorer getExplorer(String name);
	
	/**
	 * Retrieve a collection of all registered modules.  
	 */
	Collection<Explorer> getExplorers();

	/**
	 * Get the global model for templates
	 * @return
	 */
	GlobalModelContext getGlobalModel();
	
	/**
	 * Get configuration properties for this instance of explorers
	 * @return
	 */
	Configuration getConfiguration();

	/**
	 * Register a set of explorers from a set of class names
	 * @param classNames
	 */
    void registerExplorersFromClassNames(Set<String> classNames);
    
    /**
     * Get an instance of an explorer singleton
     * @param <T>
     * @param className
     * @return
     */
    <T> T getService(Class<T> className);
    
    /**
     * Register a service class with an existing instance
     * @param <T>
     * @param serviceClassName
     * @param instance
     * @return
     */
    <T> void registerService(Class<T> serviceClass, T instance);
    
    /**
     * Register a service class with a provider user to allocate an instance once requested
     * @param <T>
     * @param serviceClassName
     * @param supplier
     * @return
     */
    <T> void registerService(Class<T> serviceClass, Supplier<T> supplier);
    
    /**
     * Register a service class with it's implementation
     * @param <T>
     * @param serviceClassName
     * @param serviceImplClassName
     * @return
     */
    <T> void registerService(Class<T> serviceClass, Class<? extends T> serviceImplClassName);

    /**
     * True if an auth provider was associated with ths explorer
     * @return
     */
    boolean getHasAuthProvider();
}
