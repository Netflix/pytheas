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
import java.util.List;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.utility.DeepUnwrap;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

public class ToJsonMethod implements TemplateMethodModelEx {
    private final ObjectMapper mapper;
    
    public ToJsonMethod() {
        mapper = new ObjectMapper();
        mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
    }
    
    @Override
    public Object exec(List args) throws TemplateModelException {
        if (args.size() != 1) {
            throw new TemplateModelException("Invalid syntax: ${toJson(model)}");
        }
        
        Object obj = DeepUnwrap.unwrap((TemplateModel) args.get(0));
        if (obj == null)
            obj = args.get(0);
        
        ByteArrayOutputStream strm = new ByteArrayOutputStream();
        try {
            mapper.writeValue(strm, obj);
            return strm.toString();
        }
        catch (Exception e) {
            throw new TemplateModelException("Error converting object to JSON String ", e);
        }
    }
}
