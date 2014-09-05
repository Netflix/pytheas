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
package com.netflix.explorers.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Very simple filter to redirect the root page to the default explorer
 * @author elandau
 */
public class IndexRedirectFilter implements Filter {

    private final String defaultExplorer;
    
    public IndexRedirectFilter(String defaultExplorer) {
        this.defaultExplorer = defaultExplorer;
    }
    
    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if (httpRequest.getRequestURI().equals("/")) {
            ((HttpServletResponse) response).sendRedirect("/" + defaultExplorer);
            return;
        }
        chain.doFilter(httpRequest, response);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        // no op
    }

}
