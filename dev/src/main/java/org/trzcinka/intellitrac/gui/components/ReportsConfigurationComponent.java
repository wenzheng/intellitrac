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
import org.jetbrains.annotations.NotNull;
import org.trzcinka.intellitrac.dto.Report;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents reports configuration. Reports list is stored in IntelliJ IDEA project configuration file.
 */
@State(name = "IntelliTrac",
  storages = @Storage(id = "Reports", file = "$PROJECT_FILE$"))
public class ReportsConfigurationComponent extends AbstractListModel implements ProjectComponent, PersistentStateComponent<ReportsConfigurationComponent> {

  private static Logger logger = Logger.getInstance(ReportsConfigurationComponent.class.getName());

  private List<Report> reports;

  public ReportsConfigurationComponent() {
    reports = new ArrayList<Report>();
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
   * {@inheritDoc}
   */
  public int getSize() {
    return reports.size();
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

  public void updateReport(Report report) {
    for (int i = 0; i < reports.size(); i++) {
      Report r = reports.get(i);
      if (r.getId().equals(report.getId())) {
        reports.set(i, report);
        fireContentsChanged(this, i, i);
      }
    }
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
