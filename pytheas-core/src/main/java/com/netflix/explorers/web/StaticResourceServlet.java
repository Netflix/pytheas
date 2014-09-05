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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Deprecated
public class StaticResourceServlet extends HttpServlet {
	private static final long serialVersionUID = 751025548152775434L;

	@Override
    @SuppressWarnings("unchecked")
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
		
        PrintWriter out = response.getWriter();
        String resource = request.getRequestURI().substring(request.getContextPath().length());
        resource = resource.substring("/res".length());
        
		InputStream is = getClass().getResourceAsStream(resource);
        if (is == null) {
        	response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        else {
        	if (resource.endsWith(".js")) {
        		response.setContentType("text/javascript");
        	}
        	out.print(convertStreamToString(is));
        }
    }
	
	public String convertStreamToString(InputStream is) throws IOException {
		if (is != null) {
			Writer writer = new StringWriter();
	
			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(
	                new InputStreamReader(is, "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} 
			finally {
				is.close();
			}
			return writer.toString();
		} 
		else {        
			return "";
		}
	}
}
