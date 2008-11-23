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
import org.trzcinka.intellitrac.model.tickets.State;
import org.trzcinka.intellitrac.model.tickets.tickets_list.FilterColumnsComboModel;
import org.trzcinka.intellitrac.model.tickets.tickets_list.TicketsListModel;
import org.trzcinka.intellitrac.view.toolwindow.tickets.BaseTicketsForm;
import org.trzcinka.intellitrac.view.toolwindow.tickets.ConstantToolbarForm;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;

public class TicketsListForm extends BaseTicketsForm implements CurrentReportListener {

  private JPanel rootComponent;
  private JTable ticketsList;
  private JButton editButton;

  private ConstantToolbarForm constantToolbarForm;
  private JComboBox filterColumnsComboBox;
  private JTextField filterStringTextField;

  private FilterColumnsComboModel filterColumnsModel;
  private TableRowSorter<TicketsListModel> sorter;

  public TicketsListForm() {
    ticketsModel.getCurrentReportModel().addListener(this);
    editButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        openTicketEditor();
      }
    });
    ticketsList.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          openTicketEditor();
        }
      }
    });
  }

  private void openTicketEditor() {
    int selectedRow = ticketsList.getSelectedRow();
    if (selectedRow != -1) {
      Ticket ticket = ticketsModel.getTicketsListTableModel().getTicket(selectedRow);
      ticketsModel.getCurrentTicketModel().setCurrentTicket(ticket);
      ticketsModel.setCurrentState(State.TICKET_EDITOR);
    }
  }

  public JComponent getRootComponent() {
    return rootComponent;
  }

  private void createUIComponents() {
    constantToolbarForm = new ConstantToolbarForm();
    sorter = new TableRowSorter<TicketsListModel>(ticketsModel.getTicketsListTableModel());
    ticketsList = new JTable(ticketsModel.getTicketsListTableModel());
    ticketsList.setRowSorter(sorter);
    filterColumnsModel = new FilterColumnsComboModel();
    filterColumnsComboBox = new JComboBox(filterColumnsModel);
    filterColumnsComboBox.setSelectedIndex(0);
    filterColumnsComboBox.setRenderer(new FilterColumnsComboRenderer());
    filterStringTextField = new JTextField();
    filterStringTextField.getDocument().addDocumentListener(
      new DocumentListener() {
        public void changedUpdate(DocumentEvent e) {
          newFilter();
        }

        public void insertUpdate(DocumentEvent e) {
          newFilter();
        }

        public void removeUpdate(DocumentEvent e) {
          newFilter();
        }
      });
  }

  /**
   * Update the row filter regular expression from the expression in
   * the text box.
   */
  private void newFilter() {
    RowFilter<TicketsListModel, Object> rf;
    //If current expression doesn't parse, don't update.
    try {
      rf = RowFilter.regexFilter(filterStringTextField.getText(), filterColumnsComboBox.getSelectedIndex());
    } catch (java.util.regex.PatternSyntaxException e) {
      return;
    }
    sorter.setRowFilter(rf);
  }


  public void currentReportChanged(Report report) {
    try {
      List<Ticket> tickets;
      if (report.isNew()) {
        tickets = Collections.EMPTY_LIST;
      } else {
        tickets = TracGatewayLocator.retrieveTracGateway().retrieveTickets(report.getProperQuery());
      }
      ticketsModel.getTicketsListTableModel().updateTickets(tickets);
    } catch (ConnectionFailedException e) {
      TracGatewayLocator.handleConnectionProblem();
      ticketsModel.setCurrentState(State.REPORTS_LIST);
    }
  }

}