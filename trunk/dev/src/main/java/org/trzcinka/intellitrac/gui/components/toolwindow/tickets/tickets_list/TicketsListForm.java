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

package org.trzcinka.intellitrac.gui.components.toolwindow.tickets.tickets_list;

import com.intellij.openapi.project.Project;
import org.trzcinka.intellitrac.dto.Report;
import org.trzcinka.intellitrac.dto.Ticket;
import org.trzcinka.intellitrac.gateway.ConnectionFailedException;
import org.trzcinka.intellitrac.gateway.TracGatewayLocator;
import org.trzcinka.intellitrac.gui.components.toolwindow.DataPresenter;
import org.trzcinka.intellitrac.gui.components.toolwindow.State;
import org.trzcinka.intellitrac.gui.components.toolwindow.StateInfo;
import org.trzcinka.intellitrac.gui.components.toolwindow.StateListener;
import org.trzcinka.intellitrac.gui.components.toolwindow.tickets.ConstantToolbarForm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class TicketsListForm implements DataPresenter {

  private JPanel rootComponent;
  private JTable ticketsList;
  private JButton editButton;
  private ConstantToolbarForm constantToolbarForm;
  private TicketsListTableModel tableModel;

  private Project project;
  private StateListener stateListener;

  public TicketsListForm(Project project, final StateListener stateListener) {
    this.project = project;
    this.stateListener = stateListener;
    editButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        int selectedRow = ticketsList.getSelectedRow();
        if (selectedRow != -1) {
          Ticket ticket = tableModel.getTicket(selectedRow);
          StateInfo info = new StateInfo(State.TICKET_EDITOR, ticket);
          stateListener.stateChanged(info);
        }
      }
    });
  }

  public void updateData(Object info) {
    if (!(info instanceof Report)) {
      throw new IllegalArgumentException();
    }
    Report report = (Report) info;
    try {
      List<Ticket> tickets = TracGatewayLocator.retrieveTracGateway().retrieveTickets(report.getProperQuery());
      tableModel.updateTickets(tickets);
    } catch (ConnectionFailedException e) {
      TracGatewayLocator.handleConnectionProblem();
      StateInfo newInfo = new StateInfo(State.REPORTS_LIST, null);
      stateListener.stateChanged(newInfo);
    }

  }

  public JComponent getRootComponent() {
    return rootComponent;
  }

  private void createUIComponents() {
    constantToolbarForm = new ConstantToolbarForm(project, stateListener);
    tableModel = new TicketsListTableModel(new ArrayList<Ticket>(0));
    ticketsList = new JTable(tableModel);
  }
}