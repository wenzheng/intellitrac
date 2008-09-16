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

package org.trzcinka.intellitrac.view.toolwindow.tickets.reports_list;

import com.intellij.openapi.ui.Messages;
import org.trzcinka.intellitrac.dto.Report;
import org.trzcinka.intellitrac.model.tickets.State;
import org.trzcinka.intellitrac.view.toolwindow.tickets.BaseTicketsForm;
import org.trzcinka.intellitrac.view.toolwindow.tickets.ConstantToolbarForm;

import javax.swing.*;
import java.awt.event.*;

public class ReportsListForm extends BaseTicketsForm {

  private JPanel ticketsContent;
  private JPanel rootComponent;
  private JList reportsList;
  private JButton openButton;
  private JButton addButton;
  private JButton editButton;
  private JButton removeButton;
  private JToolBar toolbar;

  private ConstantToolbarForm constantToolbarForm;

  public ReportsListForm() {
    editButton.addActionListener(new ActionListener() {
      /**
       * Invoked when an action occurs.
       */
      public void actionPerformed(ActionEvent e) {
        Report selectedReport = (Report) reportsList.getSelectedValue();
        if (selectedReport != null) {
          State stateInfo = State.REPORT_EDITOR;
          ticketsModel.getCurrentReportModel().setCurrentReport(selectedReport);
          ticketsModel.setCurrentState(stateInfo);
        }
      }
    });
    removeButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Report selectedReport = (Report) reportsList.getSelectedValue();
        if (selectedReport != null) {
          String message = bundle.getString("tool_window.tickets.reports_list.confirm_report_removal");
          int answer = Messages.showYesNoDialog(project, message, bundle.getString("dialogs.warning"), null);
          if (answer == 0) {
            ticketsModel.getReportsListModel().removeReport(selectedReport);
          }
        }
      }
    });
    addButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        State state = State.REPORT_EDITOR;
        ticketsModel.getCurrentReportModel().setCurrentReport(new Report());
        ticketsModel.setCurrentState(state);
      }
    });
    openButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        openReport();
      }
    });
    reportsList.addComponentListener(new ComponentAdapter() {
    });
    reportsList.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          openReport();
        }
      }
    });
  }

  private void openReport() {
    Report selectedReport = (Report) reportsList.getSelectedValue();
    if (selectedReport != null) {
      ticketsModel.getCurrentReportModel().setCurrentReport(selectedReport);
      ticketsModel.setCurrentState(State.TICKETS_LIST);
    }
  }

  private void createUIComponents() {
    constantToolbarForm = new ConstantToolbarForm();
    reportsList = new JList(ticketsModel.getReportsListModel());
    reportsList.setCellRenderer(new ReportsListCellRenderer());
    reportsList.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
  }

  public JComponent getRootComponent() {
    return rootComponent;
  }

}