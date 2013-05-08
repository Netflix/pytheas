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
