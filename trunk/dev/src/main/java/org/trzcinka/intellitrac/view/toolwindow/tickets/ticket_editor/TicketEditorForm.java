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

import com.intellij.openapi.diagnostic.Logger;
import org.trzcinka.intellitrac.components.ConfigurationComponent;
import org.trzcinka.intellitrac.dto.Attachment;
import org.trzcinka.intellitrac.dto.Ticket;
import org.trzcinka.intellitrac.gateway.ConnectionFailedException;
import org.trzcinka.intellitrac.gateway.TracGatewayLocator;
import org.trzcinka.intellitrac.model.ApplicationModel;
import org.trzcinka.intellitrac.model.IntelliTracConfiguration;
import org.trzcinka.intellitrac.model.tickets.CurrentTicketListener;
import org.trzcinka.intellitrac.view.view_utils.FormUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;

public class TicketEditorForm extends BaseTicketEditorForm implements CurrentTicketListener {

  private static Logger logger = Logger.getInstance(TicketEditorForm.class.getName());

  public void currentTicketChanged(Ticket ticket) {
    synchronizeButton.setVisible(true);
    idLabel.setText(MessageFormat.format(IntelliTracConfiguration.getInstance().getConfiguration().getString("ticket_id_format"), ticket.getId()));
    summaryTextField.setText(ticket.getSummary());
    reporterLabel.setText(ticket.getReporter());
    ownerLabel.setText(ticket.getOwner());
    keywordsTextField.setText(ticket.getKeywords());
    ccTextField.setText(ticket.getCc());
    descriptionTextPane.setText(ticket.getDescription());
    descriptionTextPane.setPreviewMode();
    commentTextPane.setText(null);
    commentTextPane.setEditMode();
    changeHistoryButton.setVisible(true);

    actionsPanel.setVisible(true);
    assignToLabel.setVisible(false);
    assignToTextField.setVisible(false);
    ownersInfoPanel.setVisible(false);
    attachmentsPanel.setVisible(true);
    commentPanel.setVisible(true);


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
          t.setStatus(IntelliTracConfiguration.getInstance().getConfiguration().getString("status_upon_accepting"));
          t.setOwner(ApplicationModel.getProject().getComponent(ConfigurationComponent.class).getConnectionSettings().getLogin());
        }

        try {
          gateway.updateTicket(t, commentTextPane.getText());
        } catch (ConnectionFailedException e1) {
          TracGatewayLocator.handleConnectionProblem();
        }

        showDoneLabel();
      }


    };


  }

  public void appendTextToComment(String text) {
    commentTextPane.setText(descriptionTextPane.getText() + text);
  }

  public void appendTextToDescription(String text) {
    descriptionTextPane.setText(descriptionTextPane.getText() + text);
  }

  private void fillCombosAndChanges(Ticket ticket) {
    try {
      FormUtils.fillComboBox(componentComboBoxModel, gateway.retrieveComponents(), ticket.getComponent(), true);
      FormUtils.fillComboBox(priorityComboBoxModel, gateway.retrievePriorities(), ticket.getPriority(), true);
      FormUtils.fillComboBox(typeComboBoxModel, gateway.retrieveTypes(), ticket.getType(), true);
      FormUtils.fillComboBox(milestoneComboBoxModel, gateway.retrieveMilestones(), ticket.getMilestone(), true);
      FormUtils.fillComboBox(versionComboBoxModel, gateway.retrieveVersions(), ticket.getVersion(), true);
      FormUtils.fillComboBox(resolutionsComboBoxModel, gateway.retrieveResolutions(), ticket.getResolution(), true);
    } catch (ConnectionFailedException e) {
      TracGatewayLocator.handleConnectionProblem();
    }
  }
}