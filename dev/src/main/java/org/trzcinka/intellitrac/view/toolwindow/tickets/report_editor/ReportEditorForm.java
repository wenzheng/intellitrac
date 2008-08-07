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

package org.trzcinka.intellitrac.view.toolwindow.tickets.report_editor;

import com.intellij.openapi.project.Project;
import org.trzcinka.intellitrac.dto.Report;
import org.trzcinka.intellitrac.model.TicketsState;
import org.trzcinka.intellitrac.model.TicketsStateChangeListener;
import org.trzcinka.intellitrac.model.TicketsStateInfo;
import org.trzcinka.intellitrac.view.configuration.ReportsConfigurationComponent;
import org.trzcinka.intellitrac.view.toolwindow.tickets.BaseTicketsForm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReportEditorForm extends BaseTicketsForm implements TicketsStateChangeListener {

  private JPanel rootComponent;

  private Long id;
  private JTextField name;
  private JTextField description;
  private JTextArea query;

  private JButton okButton;
  private JButton cancelButton;

  private Project project;

  public ReportEditorForm(final Project project) {
    this.project = project;
    ticketsModel.addStateListener(this);
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
    TicketsStateInfo stateInfo = new TicketsStateInfo(TicketsState.REPORTS_LIST, null);
    ticketsModel.setCurrentState(stateInfo);
  }

  private void setData(Report data) {
    id = data.getId();
    name.setText(data.getName());
    description.setText(data.getDescription());
    query.setText(data.getQuery());
  }

  private void getData(Report data) {
    data.setId(id);
    data.setName(name.getText());
    data.setDescription(description.getText());
    data.setQuery(query.getText());
  }

  public JComponent getRootComponent() {
    return rootComponent;
  }

  public void stateChanged(TicketsStateInfo ticketsStateInfo) {
    if (ticketsStateInfo.getState() == TicketsState.REPORT_EDITOR) {
      updateData(ticketsStateInfo.getInfo());
    }
  }

  /**
   * If the editor should edit a current report, info should contain a report. If the editor
   * is to create a new one, info should be null.
   *
   * @param info info.
   */
  private void updateData(Object info) {
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