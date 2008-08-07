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

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.trzcinka.intellitrac.BundleLocator;
import org.trzcinka.intellitrac.dto.Report;
import org.trzcinka.intellitrac.model.TicketsState;
import org.trzcinka.intellitrac.model.TicketsStateChangeListener;
import org.trzcinka.intellitrac.model.TicketsStateInfo;
import org.trzcinka.intellitrac.view.configuration.ReportsConfigurationComponent;
import org.trzcinka.intellitrac.view.toolwindow.tickets.ConstantToolbarForm;

import javax.swing.*;
import java.awt.event.*;
import java.util.ResourceBundle;

public class ReportsListForm implements TicketsStateChangeListener {

  private static ApplicationModel applicationModel = ApplicationModel.getInstance();
  private static final ResourceBundle BUNDLE = BundleLocator.getBundle();

  private JPanel ticketsContent;
  private JPanel rootComponent;
  private JList reportsList;
  private JButton openButton;
  private JButton addButton;
  private JButton editButton;
  private JButton removeButton;
  private JToolBar toolbar;
  private ConstantToolbarForm constantToolbarForm;

  private Project project;
  private ReportsConfigurationComponent reportsConf;

  public ReportsListForm(final Project project) {
    this.project = project;
    applicationModel.addStateListener(this);
    editButton.addActionListener(new ActionListener() {
      /**
       * Invoked when an action occurs.
       */
      public void actionPerformed(ActionEvent e) {
        Report selectedReport = (Report) reportsList.getSelectedValue();
        if (selectedReport != null) {
          TicketsStateInfo infoTickets = new TicketsStateInfo(TicketsState.REPORT_EDITOR, selectedReport);
          applicationModel.setCurrentState(infoTickets);
        }
      }
    });
    removeButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Report selectedReport = (Report) reportsList.getSelectedValue();
        if (selectedReport != null) {
          String message = BUNDLE.getString("tool_window.tickets.reports_list.confirm_report_removal");
          int answer = Messages.showYesNoDialog(project, message, BUNDLE.getString("dialogs.warning"), null);
          if (answer == 0) {
            reportsConf.removeReport(selectedReport);
          }
        }
      }
    });
    addButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        TicketsStateInfo infoTickets = new TicketsStateInfo(TicketsState.REPORT_EDITOR, null);
        applicationModel.setCurrentState(infoTickets);
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
      TicketsStateInfo infoTickets = new TicketsStateInfo(TicketsState.TICKETS_LIST, selectedReport);
      applicationModel.setCurrentState(infoTickets);
    }
  }

  private void createUIComponents() {
    constantToolbarForm = new ConstantToolbarForm(project);
    reportsConf = project.getComponent(ReportsConfigurationComponent.class);
    reportsList = new JList(reportsConf);
    reportsList.setCellRenderer(new ReportsListCellRenderer());
    reportsList.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
  }

  public JComponent getRootComponent() {
    return rootComponent;
  }

  public void stateChanged(TicketsStateInfo ticketsStateInfo) {
  }

}