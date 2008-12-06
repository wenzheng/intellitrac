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

  public Project getProject() {
    DataContext dataContext = DataManager.getInstance().getDataContext();
    return DataKeys.PROJECT.getData(dataContext);
  }

}
