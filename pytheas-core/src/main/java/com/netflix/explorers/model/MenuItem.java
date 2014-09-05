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

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Maps;

public class MenuItem {
    private String title;
    private String name;
    private String href;
    private Map<String, MenuItem> children;
    
    public String getTitle() {
        return title;
    }
    
    public MenuItem setName(String name) {
        this.name = name;
        return this;
    }
    
    public String getName() {
        return this.name;
    }
    
    public MenuItem setTitle(String title) {
        this.title = title;
        return this;
    }
    
    public String getHref() {
        return href;
    }
    
    public MenuItem setHref(String href) {
        this.href = href;
        return this;
    }
    
    public Map<String, MenuItem> getChildren() {
        return children;
    }
    
    public MenuItem addChild(MenuItem child) {
        if (this.children == null)
            this.children = Maps.newLinkedHashMap();
        this.children.put(child.getName(), child);
        return this;
    }

    public MenuItem getChild(String name) {
        if (this.children == null)
            return null;
        return this.children.get(name);
    }
    
    public boolean hasChildren() {
        return this.children != null && !this.children.isEmpty();
    }
    
    public String toString() {
        return new StringBuilder().append("MenuItem[name=").append(name)
                                  .append(",title=").append(title)
                                  .append(",href=").append(href)
                                  .append(",children=").append(childrenToString())
                                  .append("]").toString();
    }
    
    private String childrenToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        if (children != null) {
            sb.append(StringUtils.join(children.values(), ","));
        }
        sb.append("]");
        return sb.toString();
    }
}
