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

/**
 *
 * Date: 2008-08-08
 *
 * @author Michal Trzcinka
 */
package org.trzcinka.intellitrac.model;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.project.Project;
import org.trzcinka.intellitrac.components.ConfigurationComponent;
import org.trzcinka.intellitrac.dto.ConnectionSettings;
import org.trzcinka.intellitrac.dto.DefaultValues;
import org.trzcinka.intellitrac.dto.Template;

import java.util.List;

/**
 * Represents general application model. Currently just holds current project instance.
 */
public class ApplicationModel {
  private static ApplicationModel instance = new ApplicationModel();

  public static ApplicationModel getInstance() {
    return instance;
  }

  private ApplicationModel() {
  }

  public static Project getProject() {
    DataContext dataContext = DataManager.getInstance().getDataContext();
    return DataKeys.PROJECT.getData(dataContext);
  }

  public static ConnectionSettings getConnectionSettings() {
    return getProject().getComponent(ConfigurationComponent.class).getConnectionSettings();
  }

  public static DefaultValues getDefaultValues() {
    return getProject().getComponent(ConfigurationComponent.class).getDefaultValues();
  }

  public static List<Template> getTicketTemplates() {
    return getProject().getComponent(ConfigurationComponent.class).getTicketTemplates();
  }
}
