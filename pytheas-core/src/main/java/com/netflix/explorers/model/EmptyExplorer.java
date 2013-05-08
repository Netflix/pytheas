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
package com.netflix.explorers.model;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.google.common.collect.Sets;
import com.netflix.explorers.Explorer;
import com.netflix.explorers.ExplorerManager;

public class EmptyExplorer implements Explorer {
    private static final Set<String> EMPTY_SET = Sets.newHashSet();
    
    private static final EmptyExplorer instance = new EmptyExplorer();
    public static final EmptyExplorer getInstance() {
        return instance;
    }
    
    private EmptyExplorer() {
        
    }
    
    @Override
    public void initialize() {
    }

    @Override
    public void initialize(ExplorerManager manager) {
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
    public String getName() {
        return "";
    }

    @Override
    public String getTitle() {
        return "Home";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getAlertMessage() {
        return "";
    }

    @Override
    public boolean getIsSecure() {
        return false;
    }

    @Override
    @Deprecated
    public Map<String, String> getMenuLayout() {
        return null;
    }

    @Override
    public MenuItem getMenu() {
        return null;
    }

    @Override
    public String getLayoutName() {
        return "bootstrap";
    }

    @Override
    public Properties getProperties() {
        return new Properties();
    }

    @Override
    public Set<String> getRolesAllowed() {
        return EMPTY_SET;
    }

    @Override
    public boolean getCmcEnabled() {
        return false;
    }

    @Override
    public String getHome() {
        return "";
    }
}
