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
