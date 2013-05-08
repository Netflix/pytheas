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
