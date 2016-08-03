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
package com.netflix.explorers.model;

import javax.xml.bind.annotation.XmlRootElement;

import com.netflix.explorers.Explorer;
import com.netflix.explorers.annotations.ExplorerEntity;

@XmlRootElement(name = "explorer")
@ExplorerEntity
public class ExplorerInfoEntity  {
    private String name;
    private String description;
    private String title;
    private String home;
    
    public ExplorerInfoEntity() {
        
    }
    
    public ExplorerInfoEntity(Explorer explorer) {
        name        = explorer.getName();
        description = explorer.getDescription();
        title       = explorer.getTitle();
        home        = explorer.getHome();
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getHome() {
        return home;
    }
    public void setHome(String home) {
        this.home = home;
    }

    @Override
    public String toString() {
        return "ExplorerInfoEntity [name=" + name + ", description="
                + description + ", title=" + title + ", home=" + home + "]";
    }
}
