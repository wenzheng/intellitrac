/*
 * Copyright 2008 Michal Trzcinka
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.trzcinka.intellitrac.model;

import com.intellij.openapi.diagnostic.Logger;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * This class should be used to obtain access to config.properties file.
 */
public final class IntelliTracConfiguration {

  private static final Logger logger = Logger.getInstance(IntelliTracConfiguration.class.getName());

  private static final String CONFIGURATION_FILENAME = "config.properties";

  private Configuration configuration;

  private static IntelliTracConfiguration instance;

  private IntelliTracConfiguration() {
    try {
      configuration = new PropertiesConfiguration(getClass().getClassLoader().getResource(CONFIGURATION_FILENAME));
    } catch (ConfigurationException e) {
      logger.error("Could not load configuration file", e);
    }
  }

  public static IntelliTracConfiguration getInstance() {
    if (instance == null) {
      instance = new IntelliTracConfiguration();
    }
    return instance;
  }

  public Configuration getConfiguration() {
    return configuration;
  }

}