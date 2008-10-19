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
import org.trzcinka.intellitrac.model.tickets.CurrentTicketListener;
import org.trzcinka.intellitrac.model.tickets.TicketsModel;
import org.trzcinka.intellitrac.view.toolwindow.tickets.BaseTicketsForm;
import org.trzcinka.intellitrac.view.toolwindow.tickets.ConstantToolbarForm;

import javax.swing.*;
import java.awt.event.ActionListener;

public abstract class BaseTicketEditorForm extends BaseTicketsForm implements CurrentTicketListener {

  private JPanel rootComponent;
  private ConstantToolbarForm constantToolbarForm;
  protected JTextField summaryTextField;
  private JComboBox componentComboBox;
  private JComboBox priorityComboBox;
  protected JLabel reporterLabel;
  protected JLabel idLabel;
  protected JLabel ownerLabel;
  private JComboBox typeComboBox;
  private JComboBox milestoneComboBox;
  private JComboBox versionComboBox;
  protected JTextField keywordsTextField;
  protected JTextField ccTextField;
  protected JTextPane descriptionTextPane;
  private JList changesList;
  private JRadioButton leaveRadioButton;
  protected JRadioButton resolveAsRadioButton;
  private JComboBox resolutionsComboBox;
  protected JRadioButton reassignToRadioButton;
  protected JTextField reassignedUser;
  protected JRadioButton acceptRadioButton;
  private JButton submitChangesButton;
  protected JTextPane commentTextPane;
  protected JPanel actionsPanel;
  protected JTextField assignToTextField;
  protected JLabel assignToLabel;
  protected JPanel ownersInfoPanel;
  protected JPanel attachmentsPanel;
  private JList attachmentsList;
  private JScrollPane attachmentsListScroll;

  protected DefaultComboBoxModel componentComboBoxModel;
  protected DefaultComboBoxModel priorityComboBoxModel;
  protected DefaultComboBoxModel typeComboBoxModel;
  protected DefaultComboBoxModel milestoneComboBoxModel;
  protected DefaultComboBoxModel versionComboBoxModel;
  protected DefaultComboBoxModel resolutionsComboBoxModel;
  protected DefaultListModel changesListModel;
  protected DefaultListModel attachmentsListModel;

  protected final TracGateway gateway = TracGatewayLocator.retrieveTracGateway();

  public BaseTicketEditorForm() {
    ticketsModel.getCurrentTicketModel().addListener(this);
    submitChangesButton.addActionListener(retrieveSubmitButtonActionListener());
  }

  abstract ActionListener retrieveSubmitButtonActionListener();

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
    resolutionsComboBoxModel = new DefaultComboBoxModel();
    changesListModel = new DefaultListModel();
    attachmentsListModel = new DefaultListModel();

    componentComboBox = new JComboBox(componentComboBoxModel);
    priorityComboBox = new JComboBox(priorityComboBoxModel);
    typeComboBox = new JComboBox(typeComboBoxModel);
    milestoneComboBox = new JComboBox(milestoneComboBoxModel);
    versionComboBox = new JComboBox(versionComboBoxModel);
    resolutionsComboBox = new JComboBox(resolutionsComboBoxModel);

    changesList = new JList(changesListModel);
    changesList.setCellRenderer(new TicketChangesListCellRenderer());

    attachmentsList = new JList(attachmentsListModel);
    attachmentsList.setCellRenderer(new AttachmentsListCellRenderer());
  }

  /**
   * @return Returns ticket instance for this form.
   */
  protected Ticket createTicket() {
    Ticket result = TicketsModel.getInstance().getCurrentTicketModel().getCurrentTicket();
    result.setSummary(summaryTextField.getText());
    result.setComponent((String) componentComboBoxModel.getSelectedItem());
    result.setPriority((String) priorityComboBoxModel.getSelectedItem());
    result.setType((String) typeComboBoxModel.getSelectedItem());
    result.setMilestone((String) milestoneComboBoxModel.getSelectedItem());
    result.setVersion((String) versionComboBoxModel.getSelectedItem());
    result.setKeywords(keywordsTextField.getText());
    result.setCc(ccTextField.getText());
    result.setDescription(descriptionTextPane.getText());

    return result;
  }

  /**
   * Fills given comboBoxModel with provided elements.
   *
   * @param comboBoxModel not null comboBoxModel.
   * @param elements      not null elements.
   * @param selected      element to select. If null, the first element will be selected.
   * @throws ConnectionFailedException in case of connection problems.
   */
  private void fillComboBox(DefaultComboBoxModel comboBoxModel, Iterable<String> elements, Object selected) throws ConnectionFailedException {
    comboBoxModel.removeAllElements();
    boolean empty = true;
    for (String component : elements) {
      comboBoxModel.addElement(component);
      empty = false;
    }
    if (selected != null) {
      comboBoxModel.setSelectedItem(selected);
    } else if (!empty) {
      comboBoxModel.setSelectedItem(comboBoxModel.getElementAt(0));
    }
  }

  protected void fillCombosAndChanges(Ticket ticket) {
    changesListModel.removeAllElements();
    for (TicketChange ticketChange : ticket.getChanges()) {
      changesListModel.addElement(ticketChange);
    }

    try {
      fillComboBox(componentComboBoxModel, gateway.retrieveComponents(), ticket.getComponent());
      fillComboBox(priorityComboBoxModel, gateway.retrievePriorities(), ticket.getPriority());
      fillComboBox(typeComboBoxModel, gateway.retrieveTypes(), ticket.getType());
      fillComboBox(milestoneComboBoxModel, gateway.retrieveMilestones(), ticket.getMilestone());
      fillComboBox(versionComboBoxModel, gateway.retrieveVersions(), ticket.getVersion());
      fillComboBox(resolutionsComboBoxModel, gateway.retrieveResolutions(), ticket.getResolution());
    } catch (ConnectionFailedException e) {
      TracGatewayLocator.handleConnectionProblem();
    }
  }
}