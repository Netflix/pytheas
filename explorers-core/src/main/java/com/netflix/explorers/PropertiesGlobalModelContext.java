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

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import com.netflix.config.ConfigurationManager;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.configuration.ConfigurationConverter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.netflix.explorers.context.GlobalModelContext;
import com.netflix.explorers.model.CrossLink;

public class PropertiesGlobalModelContext implements GlobalModelContext {
    private static final Logger LOG = LoggerFactory.getLogger(PropertiesGlobalModelContext.class);

    public static String PROPERTY_ENVIRONMENT_NAME    = "com.netflix.explorers.environmentName";
    public static String PROPERTY_CURRENT_REGION      = "com.netflix.explorers.currentRegion";
    public static String PROPERTY_APPLICATION_NAME    = "com.netflix.explorers.applicationName";
    public static String PROPERTY_APPLICATION_VERSION = "com.netflix.explorers.applicationVersion";
    public static String PROPERTY_IS_LOCAL            = "com.netflix.explorers.local";
    public static String PROPERTY_HOME_PAGE           = "com.netflix.explorers.homepage";
    public static String PROPERTY_DEFAULT_PORT        = "com.netflix.explorers.defaultPort";
    public static String PROPERTY_DATA_CENTER         = "com.netflix.explorers.dataCenter";
    public static String PROPERTY_DEFAULT_EXPLORER    = "com.netflix.explorers.defaultExplorer";
    
    private static final String PROPERTIES_PREFIX     = "netflix.explorers";

    private final String environmentName;
    private final String currentRegion;
    private final String applicationVersion;
    private final String applicationName;
    private final Boolean isLocal;  // this should be moved outside
    private final String homePageUrl;
    private final short  defaultPort;
    private final String defaultExplorerName;
    private final String dataCenter;
    
    private final Map<String, CrossLink> links = Maps.newHashMap();
    private final Properties properties;
    
    public PropertiesGlobalModelContext(Properties props) {
        this.properties = props;
        
        environmentName     = props.getProperty(PROPERTY_ENVIRONMENT_NAME);
        currentRegion       = props.getProperty(PROPERTY_CURRENT_REGION);
        applicationVersion  = props.getProperty(PROPERTY_APPLICATION_VERSION);
        applicationName     = props.getProperty(PROPERTY_APPLICATION_NAME);
        isLocal             = Boolean.parseBoolean(props.getProperty(PROPERTY_IS_LOCAL, "false"));
        homePageUrl         = props.getProperty(PROPERTY_HOME_PAGE);
        defaultPort         = Short.parseShort(props.getProperty(PROPERTY_DEFAULT_PORT, "8080"));
        dataCenter          = props.getProperty(PROPERTY_DATA_CENTER);
        defaultExplorerName = props.getProperty(PROPERTY_DEFAULT_EXPLORER);
        
        try {
            Map<Object, Object> dcs = ConfigurationConverter.getMap(ConfigurationConverter.getConfiguration(props).subset(PROPERTIES_PREFIX + ".dc"));
            for (Entry<Object, Object> dc : dcs.entrySet()) {
                String key  = StringUtils.substringBefore(dc.getKey().toString(),  ".");
                String attr = StringUtils.substringAfter (dc.getKey().toString(),  ".");
                
                CrossLink link = links.get(key);
                if (link == null) {
                    link = new CrossLink();
                    links.put(key,  link);
                }
                
                BeanUtils.setProperty(link, attr,  dc.getValue());
            }
        }
        catch (Exception e) {
            LOG.error("Exception ", e);
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public String getEnvironmentName() {
        return environmentName;
    }

    @Override
    public Map<String, String> getEnvironment() {
        return System.getenv();
    }

    @Override
    public Map<String, CrossLink> getCrosslinks() {
        return links;
    }

    @Override
    public String getCurrentRegion() {
        return currentRegion;
    }

    @Override
    public Properties getGlobalProperties() {
        return properties;
    }

    @Override
    public String getApplicationVersion() {
        return this.applicationVersion;
    }

    @Override
    public String getApplicationName() {
        return this.applicationName;
    }

    @Override
    public boolean getIsLocalExplorer() {
        return isLocal;
    }

    @Override
    public String getHomePageUrl() {
        return this.homePageUrl;
    }

    @Override
    public short getDefaultPort() {
        return this.defaultPort;
    }

    @Override
    public String getDataCenter() {
        return this.dataCenter;
    }

    @Override
    public long getStartTime() {
        RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
        return rb.getStartTime();
    }

    @Override
    public String getDefaultExplorerName() {
        return this.defaultExplorerName;
    }

    @Override
    public String toString() {
        return "PropertiesGlobalModelContext [environmentName=" + environmentName + ", currentRegion=" + currentRegion
                + ", applicationVersion=" + applicationVersion + ", applicationName=" + applicationName + ", isLocal="
                + isLocal + ", homePageUrl=" + homePageUrl + ", defaultPort=" + defaultPort + ", defaultExplorerName="
                + defaultExplorerName + ", dataCenter=" + dataCenter + ", links=" + links + ", properties="
                + properties + "]";
    }

}
