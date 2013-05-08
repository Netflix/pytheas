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

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.netflix.explorers.model.MenuItem;

public interface Explorer {
    /**
     * Initialize the explorer module.  Called once at startup
     */
    void initialize();
    
    /**
     * Initialize the module within the context of the provided manager.
     * @param manager
     */
    void initialize(ExplorerManager manager);
    
    /**
     * Shutdown the explorer module.  Called once at shutdown
     */
    void shutdown();

    /**
     * Suspend any threads in the module and remove it from the UI without actually shutting it down
     */
    void suspend();
    
    /**
     * Resume a suspended module
     */
    void resume();
    
    /**
     * System unique internal explorer name
     * @return
     */
    String getName(); 
    
    /**
     * Get the title to display in the UI
     * @return
     */
    String getTitle();
    
    /**
     * Get the explorer description to display in the UI
     * @return
     */
    String getDescription();
    
    /**
     * Get the alert message to display at the top of the UI.  
     * 
     * TOOD: May want to make this a template
     * @return
     */
    String getAlertMessage();
    
    /**
     * Determines if the explorer is secure and requires authentication
     * @return
     */
    boolean getIsSecure();
    
    /**
     * Return the explorer's menu layout.  The mapping is from menu name to relative
     * path in the explorer.
     * 
     *  menu1title               : menu1
     *  menu2title/submenu1title : menu2/submenu1
     *  menu2title/submenu2title : menu2/submenu2
     * 
     * @return
     */
    Map<String, String> getMenuLayout();

    MenuItem getMenu();
    
    /**
     * Return a layout name override specifically for this explorer
     * @return
     * 
     */
    String getLayoutName();
    
    /**
     * Get properties specific to this explorer
     * @return
     */
    Properties getProperties();
    
    /**
     * Get the set of allowed roles to access this explorer.  If this set is empty then all 
     * users are allowed
     * @return
     */
    Set<String> getRolesAllowed();
    
    /**
     * Return true if this explorer uses CMCs.  
     * @return
     */
    boolean getCmcEnabled();

     /**
     * Returns home url for the explorer
     * @return
     */
    String getHome();
}
