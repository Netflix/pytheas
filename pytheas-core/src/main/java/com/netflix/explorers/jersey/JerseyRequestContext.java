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
package com.netflix.explorers.jersey;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.SecurityContext;

import com.sun.jersey.spi.container.ContainerRequest;

/**
 * Request context specific to Jersey RequestFilters.
 * 
 * @author elandau
 *
 */
public class JerseyRequestContext {
    private final   ContainerRequest    context;
    private final   HttpServletRequest  request;
    private final   HttpServletResponse response;
    private String  redirectUrl;
    private SecurityContext securityContext;
            
    public JerseyRequestContext(HttpServletRequest request, HttpServletResponse response, ContainerRequest context) {
        this.request = request;
        this.response = response;
        this.context = context;
    }

    public HttpServletRequest getSevletRequest() {
        return request;
    }

    public HttpServletResponse getServletResponse() {
        return response;
    }

    public void redirect(String url) throws Exception {
        redirectUrl = url;
    }

    public void setSecurityContext(SecurityContext securityContext) {
        this.securityContext = securityContext;
        context.setSecurityContext(securityContext);
    }

    public SecurityContext getSecurityContext() {
        return this.securityContext;
    }

    public boolean isRedirecting() {
        return redirectUrl != null;
    }

    public String getRequestParameter(String parameterName) {
        return context.getFormParameters().getFirst(parameterName);
    }
    
    public String getRedirectUrl() {
        return this.redirectUrl;
    }
}
