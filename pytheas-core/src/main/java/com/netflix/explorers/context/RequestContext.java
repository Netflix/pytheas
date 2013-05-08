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
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public class RequestContext {
    private String explorerName     = "";
    private String pathToRoot       = "";
    private String explorerSubPath  = "";
    private String currentMenu      = "";
    private boolean isAjaxRequest   = false;
    private HttpServletRequest requestInvoker = null;
    
    public RequestContext() {
    }
    
    public RequestContext(HttpServletRequest requestInvoker) {
        setHttpServletRequest(requestInvoker);
    }
    
    public void setHttpServletRequest(HttpServletRequest requestInvoker) {
        this.requestInvoker = requestInvoker;
        if (requestInvoker != null) {
            String requestedWith = requestInvoker.getHeader("X-Requested-With");
            isAjaxRequest   = requestedWith != null && requestedWith.equalsIgnoreCase("XMLHttpRequest");
            pathToRoot      = this.getContextPath() + this.getServletPath();
            if (!pathToRoot.endsWith("/")) {
                pathToRoot += "/";
            }
            String pathInfo = getPathInfo();
            if (pathInfo != null) {
                String parts[] = StringUtils.split(pathInfo, "/");
                explorerSubPath = pathInfo;
                if (parts.length > 0) {
                    explorerName = parts[0];
                    explorerSubPath = pathInfo.substring(Math.min(pathInfo.length(), explorerName.length() + 2));
                    if (parts.length > 1) {
                        currentMenu = parts[1];
                    }
                }
            }
        }
    }
    
    public String getServletPath() {
        return requestInvoker.getServletPath();
    }
    
    public String getContextPath() {
        return requestInvoker.getContextPath();
    }
    
    public String getExplorerName() {
        return explorerName;
    }
    
    public RequestContext setExplorerName(String explorerName) {
        this.explorerName = explorerName;
        return this;
    }
    
    public String getPathToRoot() {
        return pathToRoot;
    }
    
    public String getPathInfo() {
        return requestInvoker.getPathInfo();
    }
    
    public String getSubPath() {
        return explorerSubPath;
    }
    public String getCurrentMenu() {
        return currentMenu;
    }
    
    public boolean getIsAjaxRequest() {
        return isAjaxRequest;
    }
    
    @Override
    public String toString() {
        return "Context [servletPath=" + getServletPath() + ", contextPath=" + getContextPath() + ", explorerName="
                + explorerName + ", pathToRoot=" + pathToRoot + ", pathInfo=" + getPathInfo() + ", explorerSubPath="
                + explorerSubPath + "]";
    }
    
}
