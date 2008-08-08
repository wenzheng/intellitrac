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
import org.trzcinka.intellitrac.model.tickets.TicketsState;
import org.trzcinka.intellitrac.model.tickets.TicketsStateChangeListener;
import org.trzcinka.intellitrac.model.tickets.TicketsStateInfo;
import org.trzcinka.intellitrac.view.toolwindow.tickets.BaseTicketsForm;
import org.trzcinka.intellitrac.view.toolwindow.tickets.ConstantToolbarForm;

import javax.swing.*;
import java.awt.event.*;

public class ReportsListForm extends BaseTicketsForm implements TicketsStateChangeListener {

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
    ticketsModel.addStateListener(this);
    editButton.addActionListener(new ActionListener() {
      /**
       * Invoked when an action occurs.
       */
      public void actionPerformed(ActionEvent e) {
        Report selectedReport = (Report) reportsList.getSelectedValue();
        if (selectedReport != null) {
          TicketsStateInfo stateInfo = new TicketsStateInfo(TicketsState.REPORT_EDITOR, selectedReport);
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
        TicketsStateInfo stateInfo = new TicketsStateInfo(TicketsState.REPORT_EDITOR, null);
        ticketsModel.setCurrentState(stateInfo);
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
      TicketsStateInfo stateInfo = new TicketsStateInfo(TicketsState.TICKETS_LIST, selectedReport);
      ticketsModel.setCurrentState(stateInfo);
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

  public void stateChanged(TicketsStateInfo ticketsStateInfo) {
    //nothing to do here
  }

}