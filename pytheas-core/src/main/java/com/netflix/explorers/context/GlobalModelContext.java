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
package com.netflix.explorers.context;

import java.util.Map;
import java.util.Properties;

import com.netflix.explorers.model.CrossLink;

/**
 * Inteface provding global context to FTL templates
 * @author elandau
 *
 */
public interface GlobalModelContext {
	/**
	 * PROD, TEST, DEV, INT
	 * @return
	 */
	String getEnvironmentName();
	
	/**
	 * 
	 * @return
	 */
	Map<String, String> getEnvironment();
	
	/**
	 * Map of datacenter names (us-east.rod, us-west, ...) to base url
	 * @return
	 */
	Map<String, CrossLink> getCrosslinks();
	
	/**
	 * us-east, eu-west, ...
	 * @return
	 */
	String getCurrentRegion();
	
	/**
	 * Global set of properties
	 * @return
	 */
	Properties getGlobalProperties();
	
	/**
	 * @return
	 */
	String getApplicationVersion();
	
	/**
	 * 
	 * @return
	 */
	String getApplicationName();
	
	/**
	 * True if this explorer only references the local instance, as apposed to a global explorer that can
	 * access all instances.
	 * @return
	 */
    boolean getIsLocalExplorer();
	
	/**
	 * Home page for the this explorer
	 * @return
	 */
	String getHomePageUrl();
	
	/**
	 * Default HTTP access port
	 * @return
	 */
	short getDefaultPort();

	/**
	 * Return the datacenter for this instance
	 * @return
	 */
	String getDataCenter();

    long getStartTime();
    
    String getDefaultExplorerName();
}
