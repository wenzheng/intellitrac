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

import org.trzcinka.intellitrac.dto.Attachment;
import org.trzcinka.intellitrac.dto.Ticket;
import org.trzcinka.intellitrac.gateway.ConnectionFailedException;
import org.trzcinka.intellitrac.gateway.TracGatewayLocator;
import org.trzcinka.intellitrac.model.IntelliTracConfiguration;
import org.trzcinka.intellitrac.model.tickets.CurrentTicketListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;

public class TicketEditorForm extends BaseTicketEditorForm implements CurrentTicketListener {

  public void currentTicketChanged(Ticket ticket) {
    idLabel.setText(MessageFormat.format(IntelliTracConfiguration.getInstance().getConfiguration().getString("ticket_id_format"), ticket.getId()));
    summaryTextField.setText(ticket.getSummary());
    reporterLabel.setText(ticket.getReporter());
    ownerLabel.setText(ticket.getOwner());
    keywordsTextField.setText(ticket.getKeywords());
    ccTextField.setText(ticket.getCc());
    descriptionTextPane.setText(ticket.getDescription());

    actionsPanel.setVisible(true);
    assignToLabel.setVisible(false);
    assignToTextField.setVisible(false);
    ownersInfoPanel.setVisible(false);
    attachmentsPanel.setVisible(true);

    attachmentsListModel.removeAllElements();
    for (Attachment attachment : ticket.getAttachments()) {
      attachmentsListModel.addElement(attachment);
    }

    fillCombosAndChanges(ticket);
  }

  ActionListener retrieveSubmitButtonActionListener() {
    return new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Ticket t = createTicket();

        if (resolveAsRadioButton.isSelected()) {
          t.setResolution((String) resolutionsComboBoxModel.getSelectedItem());
        } else if (reassignToRadioButton.isSelected()) {
          t.setOwner(reassignedUser.getText());
        } else if (acceptRadioButton.isSelected()) {
          t.setStatus("accepted"); //TODO: hardcoding
        }

        try {
          gateway.updateTicket(t, commentTextPane.getText());
        } catch (ConnectionFailedException e1) {
          TracGatewayLocator.handleConnectionProblem();
        }

      }
    };
  }
}