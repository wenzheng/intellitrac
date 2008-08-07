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

package org.trzcinka.intellitrac.view.toolwindow.tickets.ticket_editor;

import org.trzcinka.intellitrac.dto.Ticket;
import org.trzcinka.intellitrac.gateway.ConnectionFailedException;
import org.trzcinka.intellitrac.gateway.TracGatewayLocator;
import org.trzcinka.intellitrac.model.IntelliTracConfiguration;
import org.trzcinka.intellitrac.model.TicketsState;
import org.trzcinka.intellitrac.model.TicketsStateChangeListener;
import org.trzcinka.intellitrac.model.TicketsStateInfo;
import org.trzcinka.intellitrac.view.toolwindow.tickets.BaseTicketsForm;
import org.trzcinka.intellitrac.view.toolwindow.tickets.ConstantToolbarForm;

import javax.swing.*;
import java.text.MessageFormat;

public class TicketEditorForm extends BaseTicketsForm implements TicketsStateChangeListener {

  private JPanel rootComponent;
  private ConstantToolbarForm constantToolbarForm;
  private JTextField summary;
  private JComboBox componentComboBox;
  private JComboBox priorityComboBox;
  private JLabel reporter;
  private JLabel id;
  private JLabel owner;
  private JComboBox typeComboBox;
  private JComboBox milestoneComboBox;
  private JComboBox versionComboBox;
  private JTextField keywordsTextField;
  private JTextField CCTextField;
  private JTextPane description;
  private JList comments;
  private JButton addCommentButton;
  private JRadioButton leaveRadioButton;
  private JRadioButton resolveAsRadioButton;
  private JComboBox resolutions;
  private JRadioButton reassignToRadioButton;
  private JTextField reassignedUser;
  private JRadioButton acceptRadioButton;
  private JButton submitChangesButton;


  public TicketEditorForm() {
    ticketsModel.addStateListener(this);
  }

  private void loadReport(Object info) {
    if (!(info instanceof Ticket)) {
      throw new IllegalArgumentException();
    }
    Ticket ticket = (Ticket) info;
    id.setText(MessageFormat.format(IntelliTracConfiguration.getInstance().getConfiguration().getString("ticket_id_format"), ticket.getId()));
    summary.setText(ticket.getSummary());
    reporter.setText(ticket.getReporter());
    owner.setText(ticket.getOwner());
    try {
      componentComboBox.setModel(new DefaultComboBoxModel(TracGatewayLocator.retrieveTracGateway().retrieveComponents().toArray()));
    } catch (ConnectionFailedException e) {
      TracGatewayLocator.handleConnectionProblem();
    }

  }

  public JComponent getRootComponent() {
    return rootComponent;
  }

  private void createUIComponents() {
    constantToolbarForm = new ConstantToolbarForm();
  }

  public void stateChanged(TicketsStateInfo ticketsStateInfo) {
    if (ticketsStateInfo.getState() == TicketsState.TICKET_EDITOR) {
      if (ticketsStateInfo.getInfo() != null) {
        loadReport(ticketsStateInfo.getInfo());
      }
    }
  }

}