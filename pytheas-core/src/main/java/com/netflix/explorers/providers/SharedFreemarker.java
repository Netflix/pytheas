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
package com.netflix.explorers.providers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

public class SharedFreemarker {

    private static SharedFreemarker instance = new SharedFreemarker();
    public static SharedFreemarker getInstance() {
        return instance;
    }
    
    private Configuration fmConfig = new Configuration();
    
    public SharedFreemarker() {
        fmConfig.setTemplateLoader( 
                new MultiTemplateLoader(
                        new TemplateLoader[]{ 
                                new ClassTemplateLoader( getClass(), "/")}) );
        fmConfig.setNumberFormat( "0" );
        fmConfig.setLocalizedLookup( false );
        fmConfig.setTemplateUpdateDelay(0);
    }
    
    public String render(String path, Map<String, Object> model) throws TemplateException, IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        final OutputStreamWriter writer = new OutputStreamWriter( out );
        fmConfig.getTemplate(path).process(model, writer);
        return out.toString();
    }
    
}
