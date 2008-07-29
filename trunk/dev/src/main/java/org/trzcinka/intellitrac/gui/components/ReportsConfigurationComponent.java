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

package org.trzcinka.intellitrac.gui.components;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.ho.yaml.Yaml;
import org.jetbrains.annotations.NotNull;
import org.trzcinka.intellitrac.dto.Report;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 2008-07-28
 *
 * @author Michal Trzcinka
 */
@State(name = "IntelliTrac",
  storages = @Storage(id = "Reports", file = "$PROJECT_FILE$"))
public class ReportsConfigurationComponent implements ProjectComponent, PersistentStateComponent<ReportsConfigurationComponent> {

  private static Logger logger = Logger.getInstance(ReportsConfigurationComponent.class.getName());

  private static final String DEFAULT_REPORTS_FILENAME = "default_reports.yml";

  private List<Report> reports;

  public ReportsConfigurationComponent() {
    retrieveDefaultReports();

  }

  private void retrieveDefaultReports() {
    try {
      reports = Yaml.loadType(getClass().getResourceAsStream("/" + DEFAULT_REPORTS_FILENAME), ArrayList.class);
    } catch (Exception e) {
      logger.error("Could not load default reports from plugin resources. The IntelliTrac plugin is probably damaged.", e);
    }
  }

  public void initComponent() {
  }

  public void disposeComponent() {
  }

  @NotNull
  public String getComponentName() {
    return "ReportsConfigurationComponent";
  }

  public void projectOpened() {
  }

  public void projectClosed() {
  }

  /**
   * @return a component state. All properties and public fields are serialized. Only values, which differ
   *         from default (i.e. the value of newly instantiated class) are serialized.
   * @see com.intellij.util.xmlb.XmlSerializer
   */
  public ReportsConfigurationComponent getState() {
    return this;
  }

  /**
   * This method is called when new component state is loaded. A component should expect this method
   * to be called at any moment of its lifecycle. The method can and will be called several times, if
   * config files were externally changed while IDEA running.
   *
   * @param state loaded component state
   */
  public void loadState(ReportsConfigurationComponent state) {
    XmlSerializerUtil.copyBean(state, this);
  }

  //
  // Accessors
  //

  public List<Report> getReports() {
    return reports;
  }

  public void setReports(List<Report> reports) {
    this.reports = new ArrayList<Report>(reports);
  }

}
