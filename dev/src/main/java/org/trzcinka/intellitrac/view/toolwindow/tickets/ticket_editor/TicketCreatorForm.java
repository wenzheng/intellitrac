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
import org.apache.commons.lang.StringUtils;
import org.trzcinka.intellitrac.dto.DefaultValues;
import org.trzcinka.intellitrac.dto.Ticket;
import org.trzcinka.intellitrac.gateway.ConnectionFailedException;
import org.trzcinka.intellitrac.gateway.TracGatewayLocator;
import org.trzcinka.intellitrac.model.ApplicationModel;
import org.trzcinka.intellitrac.model.tickets.State;
import org.trzcinka.intellitrac.view.view_utils.FormUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicketCreatorForm extends BaseTicketEditorForm {

  private static Logger logger = Logger.getInstance(TicketCreatorForm.class.getName());

  ActionListener retrieveSubmitButtonActionListener() {
    return new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Ticket t = createTicket();

        try {
          Integer id = gateway.saveTicket(t);
          ticketsModel.getCurrentTicketModel().setCurrentTicket(gateway.retrieveTicket(id));
          ticketsModel.setCurrentState(State.TICKET_EDITOR);
          showDoneLabel();
        } catch (ConnectionFailedException e1) {
          TracGatewayLocator.handleConnectionProblem();
        }

      }
    };
  }

  public void currentTicketChanged(Ticket ticket) throws ConnectionFailedException {
    synchronizeButton.setVisible(false);
    idLabel.setText(StringUtils.EMPTY);
    summaryTextField.setText(StringUtils.EMPTY);
    reporterLabel.setText(StringUtils.EMPTY);
    ownerLabel.setText(StringUtils.EMPTY);
    keywordsTextField.setText(StringUtils.EMPTY);
    ccTextField.setText(StringUtils.EMPTY);
    descriptionTextPane.setText(ticket.getDescription());
    descriptionTextPane.setEditMode();
    commentTextPane.setText(StringUtils.EMPTY);

    changeHistoryButton.setVisible(false);
    actionsPanel.setVisible(false);
    assignToTextField.setVisible(true);
    assignToLabel.setVisible(true);
    ownersInfoPanel.setVisible(false);
    attachmentsPanel.setVisible(false);
    commentPanel.setVisible(false);

    fillCombosAndChanges();
  }


  public void appendTextToComment(String text) {
    commentTextPane.setText(descriptionTextPane.getText() + text);
  }

  public void appendTextToDescription(String text) {
    descriptionTextPane.setText(descriptionTextPane.getText() + text);
  }

  private void fillCombosAndChanges() throws ConnectionFailedException {
    DefaultValues defaultValues = ApplicationModel.getDefaultValues();
    if (defaultValues == null) {
      defaultValues = new DefaultValues();
    }
    FormUtils.fillComboBox(componentComboBoxModel, gateway.retrieveComponents(), defaultValues.getComponent(), true);
    FormUtils.fillComboBox(priorityComboBoxModel, gateway.retrievePriorities(), defaultValues.getPriority(), true);
    FormUtils.fillComboBox(typeComboBoxModel, gateway.retrieveTypes(), defaultValues.getType(), true);
    FormUtils.fillComboBox(milestoneComboBoxModel, gateway.retrieveMilestones(), defaultValues.getMilestone(), true);
    FormUtils.fillComboBox(versionComboBoxModel, gateway.retrieveVersions(), defaultValues.getVersion(), true);
    FormUtils.fillComboBox(resolutionsComboBoxModel, gateway.retrieveResolutions(), null, true);
  }
}