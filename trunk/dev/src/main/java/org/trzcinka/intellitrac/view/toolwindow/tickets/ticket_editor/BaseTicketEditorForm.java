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
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.trzcinka.intellitrac.dto.Attachment;
import org.trzcinka.intellitrac.dto.Ticket;
import org.trzcinka.intellitrac.gateway.ConnectionFailedException;
import org.trzcinka.intellitrac.gateway.TracGateway;
import org.trzcinka.intellitrac.gateway.TracGatewayLocator;
import org.trzcinka.intellitrac.model.IntelliTracConfiguration;
import org.trzcinka.intellitrac.model.tickets.CurrentTicketListener;
import org.trzcinka.intellitrac.model.tickets.TicketsModel;
import org.trzcinka.intellitrac.view.toolwindow.tickets.BaseTicketsForm;
import org.trzcinka.intellitrac.view.toolwindow.tickets.ConstantToolbarForm;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;

public abstract class BaseTicketEditorForm extends BaseTicketsForm implements CurrentTicketListener {

  private static Logger logger = Logger.getInstance(BaseTicketEditorForm.class.getName());

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
  private JRadioButton leaveRadioButton;
  protected JRadioButton resolveAsRadioButton;
  private JComboBox resolutionsComboBox;
  protected JRadioButton reassignToRadioButton;
  protected JTextField reassignedUser;
  protected JRadioButton acceptRadioButton;
  protected JButton submitChangesButton;
  protected JTextPane commentTextPane;
  protected JPanel actionsPanel;
  protected JTextField assignToTextField;
  protected JLabel assignToLabel;
  protected JPanel ownersInfoPanel;
  protected JPanel attachmentsPanel;
  private JList attachmentsList;
  JButton changeHistoryButton;
  private JButton downloadButton;
  private JButton showDescriptionButton;
  private JButton newAttachmentButton;
  protected JPanel commentPanel;
  protected JButton synchronizeButton;
  private JScrollPane attachmentsListScroll;

  protected DefaultComboBoxModel componentComboBoxModel;
  protected DefaultComboBoxModel priorityComboBoxModel;
  protected DefaultComboBoxModel typeComboBoxModel;
  protected DefaultComboBoxModel milestoneComboBoxModel;
  protected DefaultComboBoxModel versionComboBoxModel;
  protected DefaultComboBoxModel resolutionsComboBoxModel;
  protected DefaultListModel attachmentsListModel;

  protected final TracGateway gateway = TracGatewayLocator.retrieveTracGateway();

  public BaseTicketEditorForm() {
    ticketsModel.getCurrentTicketModel().addListener(this);
    submitChangesButton.addActionListener(retrieveSubmitButtonActionListener());
    changeHistoryButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Window window = SwingUtilities.getWindowAncestor(rootComponent);
        TicketChangesHistoryPopup dialog = new TicketChangesHistoryPopup(window, ticketsModel.getCurrentTicketModel().getCurrentTicket().getChanges());
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        dialog.dispose();
      }
    });
    downloadButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Attachment attachment = (Attachment) attachmentsList.getSelectedValue();
        OutputStream stream = null;
        if (attachment != null) {
          try {
            byte[] body = gateway.retrieveAttachment(ticketsModel.getCurrentTicketModel().getCurrentTicket().getId(), attachment.getFileName());
            JFileChooser fc = new JFileChooser();
            File dir = fc.getCurrentDirectory();
            fc.setSelectedFile(new File(dir, attachment.getFileName()));
            int save = fc.showSaveDialog(rootComponent);

            if (save == JFileChooser.APPROVE_OPTION) {
              File file = fc.getSelectedFile();
              stream = new FileOutputStream(file);
              IOUtils.write(body, stream);
            }
          } catch (ConnectionFailedException e1) {
            TracGatewayLocator.handleConnectionProblem();
          } catch (IOException e1) {
            logger.error("Could not save file", e1);
          } finally {
            IOUtils.closeQuietly(stream);
          }
        }
      }
    });
    showDescriptionButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Attachment attachment = (Attachment) attachmentsList.getSelectedValue();
        if (attachment != null) {
          JOptionPane popup = new AttachmentDescriptionPopup(attachment.getDescription());
          JDialog dialog = popup.createDialog(null, MessageFormat.format(bundle.getString("tool_window.tickets.ticket_editor.attachments.popup_title"), attachment.getFileName()));
          dialog.setVisible(true);
          dialog.dispose();
        }
      }
    });
    attachmentsList.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        Attachment selected = (Attachment) attachmentsList.getSelectedValue();
        if (selected == null) {
          downloadButton.setEnabled(false);
          showDescriptionButton.setEnabled(false);
        } else {
          downloadButton.setEnabled(true);
          if (!(StringUtils.isEmpty(selected.getDescription()))) {
            showDescriptionButton.setEnabled(true);
          }
        }
      }
    });
    newAttachmentButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Window window = SwingUtilities.getWindowAncestor(rootComponent);
        NewAttachmentPopup dialog = new NewAttachmentPopup(window, ticketsModel.getCurrentTicketModel().getCurrentTicket().getId());
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        dialog.dispose();
        synchronizeTicket();
      }
    });
    synchronizeButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        synchronizeTicket();
      }
    });
  }

  private void synchronizeTicket() {
    int id = ticketsModel.getCurrentTicketModel().getCurrentTicket().getId();
    try {
      Ticket t = gateway.retrieveTicket(id);
      currentTicketChanged(t);
    } catch (ConnectionFailedException e1) {
      TracGatewayLocator.handleConnectionProblem();
    }
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
    attachmentsListModel = new DefaultListModel();

    componentComboBox = new JComboBox(componentComboBoxModel);
    priorityComboBox = new JComboBox(priorityComboBoxModel);
    typeComboBox = new JComboBox(typeComboBoxModel);
    milestoneComboBox = new JComboBox(milestoneComboBoxModel);
    versionComboBox = new JComboBox(versionComboBoxModel);
    resolutionsComboBox = new JComboBox(resolutionsComboBoxModel);

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

  protected final void showDoneLabel() {
    final String lastText = submitChangesButton.getText();
    submitChangesButton.setText(bundle.getString("tool_window.tickets.ticket_editor.submit_changes.done"));
    int delay = IntelliTracConfiguration.getInstance().getConfiguration().getInt("submit_button_done_delay");
    Timer t = new Timer(delay, new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        submitChangesButton.setText(lastText);
      }
    });
    t.setRepeats(false);
    t.start();

  }
}