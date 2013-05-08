/**
 * Copyright 2013 Netflix, Inc.
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
package com.netflix.explorers.context;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

@Singleton
public class ExplorerPropertiesConfiguration extends PropertiesConfiguration {

	public static final String EXPLORERS_CONFIG_FILE_SUFFIX = "-explorers-config.properties";

	@Inject
	public ExplorerPropertiesConfiguration(@Named("explorerAppName") String explorerAppName) throws ConfigurationException {
		super(explorerAppName + EXPLORERS_CONFIG_FILE_SUFFIX);
	}

}