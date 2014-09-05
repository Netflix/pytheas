/**
 * Copyright 2014 Netflix, Inc.
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

import com.netflix.explorers.model.ExplorerInfoEntity;
import com.netflix.explorers.model.MenuItem;

public class RemoteExplorerModule implements Explorer {

    private final ExplorerManager manager;
    private final ExplorerInfoEntity entity;
    
    public RemoteExplorerModule(ExplorerManager manager, ExplorerInfoEntity entity) {
        this.manager = manager;
        this.entity  = entity;
    }

    @Override
    public void initialize() {
        manager.registerExplorer(this);
    }

    @Override
    public void initialize(ExplorerManager manager) {
    }

    @Override
    public void shutdown() {
        manager.unregisterExplorer(this);
    }

    @Override
    public void suspend() {
    }

    @Override
    public void resume() {
    }

    @Override
    public String getName() {
        return entity.getName();
    }

    @Override
    public String getTitle() {
        return entity.getTitle();
    }

    @Override
    public String getDescription() {
        return entity.getName();
    }

    @Override
    public String getAlertMessage() {
        return null;
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
        return null;
    }

    @Override
    public Properties getProperties() {
        return null;
    }

    @Override
    public Set<String> getRolesAllowed() {
        return null;
    }

    @Override
    public boolean getCmcEnabled() {
        return false;
    }

    @Override
    public String getHome() {
        return entity.getHome();
    }

    @Override
    public String toString() {
        return "RemoteExplorerModule entity = " + entity;
    }

}
