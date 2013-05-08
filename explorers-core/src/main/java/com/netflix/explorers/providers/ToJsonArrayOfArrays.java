package com.netflix.explorers.providers;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.utility.DeepUnwrap;

public class ToJsonArrayOfArrays implements TemplateMethodModelEx {
    private final ObjectMapper mapper;
    
    public ToJsonArrayOfArrays() {
        mapper = new ObjectMapper();
        mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Object exec(List args) throws TemplateModelException {
        if (args.size() != 1) {
            throw new TemplateModelException("Invalid syntax: ${toJson(model)}");
        }
        
        Map map;
        if (args.get(0) instanceof ObjectWrapper) {
            map = (Map)DeepUnwrap.unwrap((TemplateModel) args.get(0));
        }
        else {
            map = (Map)args.get(0);
        }
        
        ByteArrayOutputStream strm = new ByteArrayOutputStream();
        try {
            List ar = Lists.newArrayList(Collections2.transform(map.entrySet(), new Function<Map.Entry, List<String>>() {
                @Override
                public List<String> apply(@Nullable Entry input) {
                    return Lists.newArrayList(input.getKey().toString(), input.getValue().toString());
                }
            }));
            
            mapper.writeValue(strm, ar);
            return strm.toString();
        }
        catch (Exception e) {
            throw new TemplateModelException("Error converting object to JSON String ", e);
        }
    }
}
