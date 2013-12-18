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

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import com.netflix.config.ConfigurationManager;
import com.netflix.config.DynamicPropertyFactory;
import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.netflix.explorers.model.MenuItem;

import javax.annotation.PostConstruct;
import com.google.inject.Singleton;

import com.netflix.config.DynamicBooleanProperty;

public class AbstractExplorerModule implements Explorer {
    private static Logger LOG = LoggerFactory.getLogger(AbstractExplorerModule.class);

    private final String name;
    private String title;
    private String description;
    private String layoutName;
    private boolean isSecure;
    private MenuItem rootMenu;
    private Set<String> rolesAllowed;
    private boolean isCmcEnabled;

    private static final String[] propertyNameForms = {
        "/$NAME$-explorer.properties",
        "/$NAME$explorer.properties",
        "$NAME$-explorer.properties",
        "$NAME$explorer.properties"
    };

    private AbstractConfiguration config;
    
    public AbstractExplorerModule(String name) {
        this.name = name;
    }
    
    @Override
    public void initialize(ExplorerManager manager) {
        LOG.info("Initialize with manager " + name);
        initialize();
    }

    public void loadPropertiesFile(String filename) throws IOException {
        ConfigurationManager.loadPropertiesFromResources(filename);
    }

    @Override
    @PostConstruct
    public void initialize() {
        LOG.info("Initialize " + name);

        loadPropertiesFileFromForms();

        String prefix = "com.netflix.explorers." + name + ".";
        config = getConfigInstance();

        title       = config.getString(prefix + "title",       name + " (MissingTitle)");
        description = config.getString(prefix + "description", name + " (MissingDescription)");
        layoutName  = config.getString(prefix + "pageLayout");
        isCmcEnabled = config.getBoolean(prefix + "cmcEnabled", false);
        isSecure = config.getBoolean(prefix + "secure", false);
        rolesAllowed = Sets.newHashSet(StringUtils.split(config.getString(prefix + "rolesAllowed", ""), ","));

        String items  = config.getString(prefix + "menu");
        Map<String, String> menuLayout = getMenuLayout();
        if (menuLayout != null) {
            rootMenu = new MenuItem().setName("root").setTitle("root");
            for (Entry<String, String> item : menuLayout.entrySet()) {
                // Parse the menu hierarchy and build it if necessary
                String[] nameComponents = StringUtils.split(item.getKey(), "/");
                String[] pathComponents = StringUtils.split(item.getValue(), "/");
                if (nameComponents.length == 1) {
                    rootMenu.addChild(new MenuItem()
                        .setName(item.getKey())
                        .setHref(item.getValue())
                        .setTitle(item.getKey()));
                }
                else {
                    if (nameComponents.length != pathComponents.length) 
                        LOG.error("Name and path must have same number of components: " + item.getKey() + " -> " + item.getValue());
                    
                    MenuItem node = rootMenu;
                    for (int i = 0; i < nameComponents.length - 1; i++) {
                        MenuItem next = node.getChild(pathComponents[i]);
                        if (next == null) {
                            next = new MenuItem()
                                .setName(pathComponents[i])
                                .setTitle(nameComponents[i]);
                            node.addChild(next);
                        }
                        node = next;
                    }
                    
                    MenuItem leaf = new MenuItem()
                        .setName(pathComponents[pathComponents.length - 1])
                        .setHref(item.getValue())
                        .setTitle(nameComponents[nameComponents.length - 1]);
                    // Add the menu item to the leaf
                    node.addChild(leaf);
                }
            }
        }
        else if (items != null) {
            rootMenu = new MenuItem().setName("root").setTitle("root");
            for (String item : StringUtils.split(items, ",")) {
                String[] parts = StringUtils.splitByWholeSeparator(item, "->");
                try {
                    String[] nameComponents = StringUtils.split(parts[0].trim(), "/");
                    MenuItem node = rootMenu;
                    for (int i = 0; i < nameComponents.length; i++) {
                        MenuItem next = node.getChild(nameComponents[i]);
                        if (next == null) {
                            next = new MenuItem()
                                .setName(nameComponents[i])
                                .setTitle(nameComponents[i]);
                            node.addChild(next);
                        }
                        node = next;
                    }
                    node.setHref(parts[1].trim());
                }
                catch (Exception e) {
                    LOG.error("Failed to load menu for explorer " + name, e);
                }
            }
        }
        LOG.info(toString());
    }

    protected void loadPropertiesFileFromForms()
    {
        for ( int i = 0; i < propertyNameForms.length; ++i ) {
            String form = propertyNameForms[i];
            try {
                loadPropertiesFile(form.replace("$NAME$", name));
                break;
            } catch (Exception e) {
                if ( (i + 1) >= propertyNameForms.length ) {
                    LOG.error("Failed to open property file for " + name, e);
                }
            }
        }
    }

    protected AbstractConfiguration getConfigInstance()
    {
        return ConfigurationManager.getConfigInstance();
    }

    @Override
    public void shutdown() {
    }

    @Override
    public void suspend() {
    }

    @Override
    public void resume() {
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public final String getDescription() {
        return description;
    }

    @Override
    public String getAlertMessage() {
        return config.getString("com.netflix.explorers." + name + ".alert_message");
    }

    @Override
    public Map<String, String> getMenuLayout() {
        return null;
    }

    @Override
    public String getLayoutName() {
        return layoutName;
    }

    @Override
    public MenuItem getMenu() {
        return rootMenu;
    }

    @Override
    public boolean getIsSecure() {
        // get value from netflixConfiguration as it can be overridden for debugging / testing applications
        String propName = "com.netflix.explorers." + name + "." + "secure";
        try {
            final DynamicBooleanProperty booleanProperty = DynamicPropertyFactory.getInstance().getBooleanProperty(propName, isSecure);
            isSecure = booleanProperty.get();
            return isSecure;
        } catch (Exception ex) {
            return isSecure;
        }
    }
    

    @Override
    public Properties getProperties() {
        return null;
    }

    @Override
    public Set<String> getRolesAllowed() {
        return rolesAllowed;
    }

    @Override
    public boolean getCmcEnabled() {
        return isCmcEnabled;
    }

    @Override
    public String getHome() {
        return name; // by default home = /<exp-name>
    }

    @Override
    public String toString() {
        return "AbstractExplorerModule [name=" + name + ", title=" + title + ", description=" + description
                + ", layoutName=" + layoutName + ", isSecure=" + isSecure + ", rootMenu=" + rootMenu
                + ", rolesAllowed=" + rolesAllowed + "]";
    }

}
