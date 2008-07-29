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

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.project.Project;
import org.trzcinka.intellitrac.gui.components.ReportsConfigurationComponent;

import javax.swing.*;

public class TicketsForm {

  private JToolBar ticketsToolbar;
  private JPanel constantToolbar;
  private JButton newTicket;
  private JButton goHome;
  private JButton goBack;
  private JPanel ticketsContent;
  private JPanel rootComponent;
  private JList reportsList;

  private Project project;

  public TicketsForm(Project project) {
    this.project = project;
  }

  private void createUIComponents() {
    ReportsConfigurationComponent reportsConf = project.getComponent(ReportsConfigurationComponent.class);
    reportsList = new JList(reportsConf.getReports().toArray());

    reportsList.setCellRenderer(new ReportsListCellRenderer());

  }
}