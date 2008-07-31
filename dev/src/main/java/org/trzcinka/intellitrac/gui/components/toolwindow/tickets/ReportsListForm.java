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

import org.trzcinka.intellitrac.gui.components.ReportsConfigurationComponent;
import org.trzcinka.intellitrac.gui.components.toolwindow.StateListener;
import org.trzcinka.intellitrac.gui.components.toolwindow.StateInfo;
import org.trzcinka.intellitrac.gui.components.toolwindow.State;
import org.trzcinka.intellitrac.gui.components.toolwindow.StateData;
import org.trzcinka.intellitrac.dto.Report;

import javax.swing.*;

import com.intellij.openapi.project.Project;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Map;
import java.util.HashMap;

public class ReportsListForm {

  private JPanel ticketsContent;
  private JPanel rootComponent;
  private JList reportsList;
  private JToolBar toolBar;
  private JButton openButton;
  private JButton addButton;
  private JButton editButton;
  private JButton removeButton;
  private Project project;
  private StateListener stateListener;

  public ReportsListForm(Project project, final StateListener stateListener) {
    this.project = project;
    this.stateListener = stateListener;
    editButton.addActionListener(new ActionListener() {
      /**
       * Invoked when an action occurs.
       */
      public void actionPerformed(ActionEvent e) {
        Report selectedReport = (Report) reportsList.getSelectedValue();
        if (selectedReport != null) {
          Map<StateData, Object> stateData = new HashMap<StateData, Object>();
          stateData.put(StateData.REPORT, selectedReport);
          StateInfo info = new StateInfo(State.REPORT_EDITOR, stateData);
          stateListener.stateChanged(info);
        }
      }
    });
  }

  private void createUIComponents() {   
    ReportsConfigurationComponent reportsConf = project.getComponent(ReportsConfigurationComponent.class);
    reportsList = new JList(reportsConf.getReports().toArray());
    reportsList.setCellRenderer(new ReportsListCellRenderer());

  }

  public JComponent getRootComponent() {
    return rootComponent;
  }

}