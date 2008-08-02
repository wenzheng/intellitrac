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

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents reports configuration. Reports list is stored in IntelliJ IDEA project configuration file.
 */
@State(name = ReportsConfigurationComponent.COMPONENT_NAME,
  storages = @Storage(id = "Reports", file = "$PROJECT_FILE$"))
public class ReportsConfigurationComponent extends AbstractListModel implements ProjectComponent, PersistentStateComponent<ReportsConfigurationComponent> {

  static final String COMPONENT_NAME = "IntelliTrac.ReportsConfigurationComponent";

  private static final String DEFAULT_REPORTS_FILENAME = "default_reports.yml";

  private static Logger logger = Logger.getInstance(ReportsConfigurationComponent.class.getName());

  private List<Report> reports;

  public ReportsConfigurationComponent() {
    initializeDefaultReports();
  }

  private void initializeDefaultReports() {
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
    return COMPONENT_NAME;
  }

  public void projectOpened() {
  }

  public void projectClosed() {
  }


  /**
   * {@inheritDoc}
   */
  public int getSize() {
    return reports != null ? reports.size() : 0;
  }

  /**
   * {@inheritDoc}
   */
  public Object getElementAt(int index) {
    return reports.get(index);
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

  /**
   * Saves a report - adds a new one if its ID is null, otherwise performs update.
   *
   * @param report must be not null.
   */
  public void saveReport(Report report) {
    if (report.getId() != null) {
      updateReport(report);
    } else {
      doSaveReport(report);
    }
  }

  private void doSaveReport(Report report) {
    long id = retrieveNextReportId();
    report.setId(id);
    reports.add(report);
    int index = reports.indexOf(report);
    fireIntervalAdded(this, index, index);
  }

  private long retrieveNextReportId() {
    long maxId = 1;
    for (Report report : reports) {
      if (report.getId() > maxId) {
        maxId = report.getId();
      }
    }
    return maxId + 1;
  }

  private void updateReport(Report report) {
    for (int i = 0; i < reports.size(); i++) {
      Report r = reports.get(i);
      if (r.getId().equals(report.getId())) {
        reports.set(i, report);
        fireContentsChanged(this, i, i);
      }
    }
  }

  /**
   * Removes given report.
   *
   * @param report must be not null.
   */
  public void removeReport(Report report) {
    int index = -1;
    for (int i = 0; i < reports.size(); i++) {
      Report r = reports.get(i);
      if (r == report) {
        index = i;
      }
    }
    if (index != -1) {
      reports.remove(index);
      fireIntervalRemoved(this, index, index);
    }
  }

  //
  // Accessors
  //

  public List<Report> getReports() {
    return reports;
  }

  public void setReports(List<Report> reports) {
    this.reports = reports;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(getClass().equals(obj.getClass()))) {
      return false;
    }
    ReportsConfigurationComponent component = (ReportsConfigurationComponent) obj;
    return reports.equals(component.getReports());
  }


}
