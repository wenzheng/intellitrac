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
import org.trzcinka.intellitrac.dto.Ticket;
import org.trzcinka.intellitrac.gui.components.toolwindow.DataPresenter;
import org.trzcinka.intellitrac.gui.components.toolwindow.StateListener;
import org.trzcinka.intellitrac.gui.components.toolwindow.tickets.tickets_list.TicketsListTableModel;
import org.trzcinka.intellitrac.gateway.TracGatewayLocator;
import org.trzcinka.intellitrac.gateway.ConnectionFailedException;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class TicketsListForm implements DataPresenter {

  private JPanel rootComponent;
  private JToolBar toolBar;
  private JTable ticketsList;
  private TicketsListTableModel tableModel;

  private Project project;
  private StateListener stateListener;

  public TicketsListForm(Project project, StateListener stateListener) {
    this.project = project;
    this.stateListener = stateListener;
  }

  public void updateData(Object info) {
    if (!(info instanceof Report)) {
      throw new IllegalArgumentException();
    }
    Report report = (Report) info;
    try {
      List<Ticket> tickets = TracGatewayLocator.retrieveTracGateway().retrieveTickets(report.getQuery());
      tableModel.updateTickets(tickets);
    } catch (ConnectionFailedException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
    
  }

  public JComponent getRootComponent() {
    return rootComponent;
  }

  private void createUIComponents() {
    tableModel = new TicketsListTableModel(new ArrayList<Ticket>(0));
    ticketsList = new JTable(tableModel);
  }
}