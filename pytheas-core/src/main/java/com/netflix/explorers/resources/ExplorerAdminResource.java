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
package com.netflix.explorers.resources;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.configuration.ConfigurationConverter;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.netflix.explorers.Explorer;
import com.netflix.explorers.ExplorerManager;
import com.netflix.explorers.RemoteExplorerModule;
import com.netflix.explorers.model.ExplorerInfoEntity;
import com.netflix.explorers.model.ExplorerInfoListEntity;
import com.netflix.explorers.model.MapEntity;

@Path("admin")
@Produces( MediaType.APPLICATION_JSON )
@Consumes( MediaType.APPLICATION_JSON )
public class ExplorerAdminResource {

    @Inject(optional=true) ExplorerManager manager;

    @GET
    @Path("explorers/list")
    public ExplorerInfoListEntity listExplorers() {
        List<ExplorerInfoEntity> list = Lists.newArrayList();
        
        for (Explorer explorer : manager.getExplorers()) {
            if (!(explorer instanceof RemoteExplorerModule)) {
                list.add(new ExplorerInfoEntity(explorer));
            }
        }
    
        ExplorerInfoListEntity response = new ExplorerInfoListEntity();
        response.setExplorers(list);
        return response;
    }

    @GET
    @Path("explorers/config")
    public MapEntity getConfig() {
        return new MapEntity(ConfigurationConverter.getMap(manager.getConfiguration()));
    }
}
