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
package com.netflix.explorers.guice;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.netflix.config.ConfigurationManager;
import com.netflix.explorers.AppConfigGlobalModelContext;
import com.netflix.explorers.ExplorerManager;
import com.netflix.explorers.ExplorersManagerImpl;
import com.netflix.explorers.annotations.ExplorerGuiceModule;
import com.netflix.explorers.context.GlobalModelContext;


@ExplorerGuiceModule
public class BaseExplorerGuiceModule extends AbstractModule {

    public static final String APP_ID = "archaius.deployment.applicationId";

    @Override
    protected void configure() {
        final String appId = ConfigurationManager.getConfigInstance().getString(APP_ID);
        bind(String.class).annotatedWith(Names.named("explorerAppName")).toInstance(appId);
        bind(GlobalModelContext.class).to(AppConfigGlobalModelContext.class);
        bind(ExplorerManager.class).to(ExplorersManagerImpl.class);
    }
}