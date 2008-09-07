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
import org.trzcinka.intellitrac.dto.TicketChange;
import org.trzcinka.intellitrac.gateway.ConnectionFailedException;
import org.trzcinka.intellitrac.gateway.TracGateway;
import org.trzcinka.intellitrac.gateway.TracGatewayLocator;
import org.trzcinka.intellitrac.model.IntelliTracConfiguration;
import org.trzcinka.intellitrac.model.tickets.CurrentTicketListener;
import org.trzcinka.intellitrac.model.tickets.TicketsModel;
import org.trzcinka.intellitrac.view.toolwindow.tickets.BaseTicketsForm;
import org.trzcinka.intellitrac.view.toolwindow.tickets.ConstantToolbarForm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;

public class TicketEditorForm extends BaseTicketsForm implements CurrentTicketListener {

  private JPanel rootComponent;
  private ConstantToolbarForm constantToolbarForm;
  private JTextField summaryTextField;
  private JComboBox componentComboBox;
  private JComboBox priorityComboBox;
  private JLabel reporterLabel;
  private JLabel idLabel;
  private JLabel ownerLabel;
  private JComboBox typeComboBox;
  private JComboBox milestoneComboBox;
  private JComboBox versionComboBox;
  private JTextField keywordsTextField;
  private JTextField ccTextField;
  private JTextPane descriptionTextPane;
  private JList changes;
  private JRadioButton leaveRadioButton;
  private JRadioButton resolveAsRadioButton;
  private JComboBox resolutions;
  private JRadioButton reassignToRadioButton;
  private JTextField reassignedUser;
  private JRadioButton acceptRadioButton;
  private JButton submitChangesButton;
  private JTextPane commentTextPane;

  private DefaultComboBoxModel componentComboBoxModel;
  private DefaultComboBoxModel priorityComboBoxModel;
  private DefaultComboBoxModel typeComboBoxModel;
  private DefaultComboBoxModel milestoneComboBoxModel;
  private DefaultComboBoxModel versionComboBoxModel;
  private DefaultListModel changesModel;

  private final TracGateway gateway = TracGatewayLocator.retrieveTracGateway();

  public TicketEditorForm() {
    ticketsModel.getCurrentTicketModel().addListener(this);
    submitChangesButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Ticket t = new Ticket();

      }
    });
  }

  public JComponent getRootComponent() {
    return rootComponent;
  }

  private void createUIComponents() {
    constantToolbarForm = new ConstantToolbarForm();
    componentComboBoxModel = new DefaultComboBoxModel();
    priorityComboBoxModel = new DefaultComboBoxModel();
    typeComboBoxModel = new DefaultComboBoxModel();
    milestoneComboBoxModel = new DefaultComboBoxModel();
    versionComboBoxModel = new DefaultComboBoxModel();
    changesModel = new DefaultListModel();

    componentComboBox = new JComboBox(componentComboBoxModel);
    priorityComboBox = new JComboBox(priorityComboBoxModel);
    typeComboBox = new JComboBox(typeComboBoxModel);
    milestoneComboBox = new JComboBox(milestoneComboBoxModel);
    versionComboBox = new JComboBox(versionComboBoxModel);
    changes = new JList(changesModel);
    changes.setCellRenderer(new TicketChangesListCellRenderer());

  }

  public void currentTicketChanged(Ticket ticket) {
    idLabel.setText(MessageFormat.format(IntelliTracConfiguration.getInstance().getConfiguration().getString("ticket_id_format"), ticket.getId()));
    summaryTextField.setText(ticket.getSummary());
    reporterLabel.setText(ticket.getReporter());
    ownerLabel.setText(ticket.getOwner());
    keywordsTextField.setText(ticket.getKeywords());
    ccTextField.setText(ticket.getCc());
    descriptionTextPane.setText(ticket.getDescription());

    changesModel.removeAllElements();
    for (TicketChange ticketChange : ticket.getChanges()) {
      changesModel.addElement(ticketChange);
    }

    try {
      fillComboBox(componentComboBoxModel, gateway.retrieveComponents(), ticket.getComponent());
      fillComboBox(priorityComboBoxModel, gateway.retrievePriorities(), ticket.getPriority());
      fillComboBox(typeComboBoxModel, gateway.retrieveTypes(), ticket.getType());
      fillComboBox(milestoneComboBoxModel, gateway.retrieveMilestones(), ticket.getMilestone());
      fillComboBox(versionComboBoxModel, gateway.retrieveVersions(), ticket.getVersion());
    } catch (ConnectionFailedException e) {
      TracGatewayLocator.handleConnectionProblem();
    }
  }

  /**
   * @return Returns ticket instance for this form.
   */
  private Ticket createTicket() {
    Ticket result = TicketsModel.getInstance().getCurrentTicketModel().getCurrentTicket();
    result.setSummary(summaryTextField.getText());
    result.setComponent((String) componentComboBoxModel.getSelectedItem());
    result.setPriority((String) priorityComboBoxModel.getSelectedItem());
    result.setType((String) milestoneComboBoxModel.getSelectedItem());
    result.setVersion((String) versionComboBoxModel.getSelectedItem());
    result.setKeywords(keywordsTextField.getText());
    result.setCc(ccTextField.getText());

    return result;
  }

  private void fillComboBox(DefaultComboBoxModel comboBoxModel, Iterable<String> elements, Object selected) throws ConnectionFailedException {
    comboBoxModel.removeAllElements();
    for (String component : elements) {
      comboBoxModel.addElement(component);
    }
    comboBoxModel.setSelectedItem(selected);
  }

}