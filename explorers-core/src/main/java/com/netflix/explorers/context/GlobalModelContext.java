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
