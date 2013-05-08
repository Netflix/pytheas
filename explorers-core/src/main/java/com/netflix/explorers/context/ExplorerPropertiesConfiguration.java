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