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

import org.trzcinka.intellitrac.dto.Report;
import org.trzcinka.intellitrac.model.tickets.CurrentReportListener;
import org.trzcinka.intellitrac.model.tickets.State;
import org.trzcinka.intellitrac.view.toolwindow.tickets.BaseTicketsForm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReportEditorForm extends BaseTicketsForm implements CurrentReportListener {

  private JPanel rootComponent;

  private Long id;
  private JTextField name;
  private JTextField description;
  private JTextArea query;

  private JButton okButton;
  private JButton cancelButton;

  public ReportEditorForm() {
    ticketsModel.getCurrentReportModel().addListener(this);
    okButton.addActionListener(new ActionListener() {
      /**
       * Invoked when an action occurs.
       */
      public void actionPerformed(ActionEvent e) {
        Report report = new Report();
        getData(report);
        ticketsModel.getReportsListModel().saveReport(report);
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
    State state = State.REPORTS_LIST;
    ticketsModel.setCurrentState(state);
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

  public void currentReportChanged(Report report) {
    setData(report);
  }

}