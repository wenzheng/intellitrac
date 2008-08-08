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

package org.trzcinka.intellitrac.view.toolwindow.tickets.tickets_list;

import org.trzcinka.intellitrac.dto.Report;
import org.trzcinka.intellitrac.dto.Ticket;
import org.trzcinka.intellitrac.gateway.ConnectionFailedException;
import org.trzcinka.intellitrac.gateway.TracGatewayLocator;
import org.trzcinka.intellitrac.model.tickets.CurrentReportListener;
import org.trzcinka.intellitrac.model.tickets.TicketsState;
import org.trzcinka.intellitrac.view.toolwindow.tickets.BaseTicketsForm;
import org.trzcinka.intellitrac.view.toolwindow.tickets.ConstantToolbarForm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TicketsListForm extends BaseTicketsForm implements CurrentReportListener {

  private JPanel rootComponent;
  private JTable ticketsList;
  private JButton editButton;

  private ConstantToolbarForm constantToolbarForm;

  public TicketsListForm() {
    ticketsModel.getCurrentReportModel().addListener(this);
    editButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        int selectedRow = ticketsList.getSelectedRow();
        if (selectedRow != -1) {
          Ticket ticket = ticketsModel.getTicketsListTableModel().getTicket(selectedRow);
          ticketsModel.getCurrentTicketModel().setCurrentTicket(ticket);
          ticketsModel.setCurrentState(TicketsState.TICKET_EDITOR);
        }
      }
    });
  }

  public JComponent getRootComponent() {
    return rootComponent;
  }

  private void createUIComponents() {
    constantToolbarForm = new ConstantToolbarForm();
    ticketsList = new JTable(ticketsModel.getTicketsListTableModel());
  }

  public void currentReportChanged(Report report) {
    try {
      List<Ticket> tickets = TracGatewayLocator.retrieveTracGateway().retrieveTickets(report.getProperQuery());
      ticketsModel.getTicketsListTableModel().updateTickets(tickets);
    } catch (ConnectionFailedException e) {
      TracGatewayLocator.handleConnectionProblem();
      ticketsModel.setCurrentState(TicketsState.REPORTS_LIST);
    }
  }

}