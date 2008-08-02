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

package org.trzcinka.intellitrac.gui.components.toolwindow.tickets;

import com.intellij.openapi.project.Project;
import org.trzcinka.intellitrac.dto.Report;
import org.trzcinka.intellitrac.gui.components.ReportsConfigurationComponent;
import org.trzcinka.intellitrac.gui.components.toolwindow.DataPresenter;
import org.trzcinka.intellitrac.gui.components.toolwindow.State;
import org.trzcinka.intellitrac.gui.components.toolwindow.StateInfo;
import org.trzcinka.intellitrac.gui.components.toolwindow.StateListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReportEditorForm implements DataPresenter {

  private JPanel rootComponent;

  private Long id;
  private JTextField name;
  private JTextField description;
  private JTextArea query;

  private JButton okButton;
  private JButton cancelButton;

  private Project project;
  private StateListener stateListener;


  public ReportEditorForm(final Project project, final StateListener stateListener) {
    this.project = project;
    this.stateListener = stateListener;
    okButton.addActionListener(new ActionListener() {
      /**
       * Invoked when an action occurs.
       */
      public void actionPerformed(ActionEvent e) {
        ReportsConfigurationComponent reportsConf = project.getComponent(ReportsConfigurationComponent.class);
        Report report = new Report();
        getData(report);
        reportsConf.saveReport(report);
        reportsListRedirect();
      }
    });
    cancelButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        reportsListRedirect();
      }
    });
  }

  private void reportsListRedirect() {
    StateInfo info = new StateInfo(State.REPORTS_LIST, null);
    stateListener.stateChanged(info);
  }

  public void setData(Report data) {
    id = data.getId();
    name.setText(data.getName());
    description.setText(data.getDescription());
    query.setText(data.getQuery());
  }

  public void getData(Report data) {
    data.setId(id);
    data.setName(name.getText());
    data.setDescription(description.getText());
    data.setQuery(query.getText());
  }

  public JComponent getRootComponent() {
    return rootComponent;
  }

  /**
   * If the editor should edit a current report, info should contain a report. If the editor
   * is to create a new one, info should be null.
   *
   * @param info info.
   */
  public void updateData(Object info) {
    Report report;
    if (info != null) {
      if (!(info instanceof Report)) {
        throw new IllegalArgumentException();
      }
      report = (Report) info;
    } else {
      report = new Report();
    }
    setData(report);

  }

}