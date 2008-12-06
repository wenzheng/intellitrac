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

package org.trzcinka.intellitrac.view.configuration;

import com.intellij.openapi.ui.Messages;
import com.intellij.ui.DocumentAdapter;
import org.trzcinka.intellitrac.BundleLocator;
import org.trzcinka.intellitrac.dto.*;
import org.trzcinka.intellitrac.gateway.ConnectionFailedException;
import org.trzcinka.intellitrac.gateway.TracGateway;
import org.trzcinka.intellitrac.gateway.TracGatewayLocator;
import org.trzcinka.intellitrac.utils.CollectionsUtils;
import org.trzcinka.intellitrac.utils.DeepCopyUtils;
import static org.trzcinka.intellitrac.view.view_utils.FormUtils.fillComboBox;
import org.trzcinka.intellitrac.view.view_utils.MouseCursors;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.util.ResourceBundle;

/**
 * IntelliTrac configuration form. The configuration is per-project - it is shown under
 * project settings.
 */
public class ConfigurationForm {

  private static TracGateway gateway = TracGatewayLocator.retrieveTracGateway();

  private JPanel connectionPanel;
  private JTextField tracUrl;
  private JTextField login;
  private JTextField password;
  private JButton testConnectionButton;
  private JTabbedPane rootComponent;

  private JComboBox componentComboBox;
  private JComboBox priorityComboBox;
  private JComboBox typeComboBox;
  private JComboBox milestoneComboBox;
  private JComboBox versionComboBox;
  private JList ticketTemplatesList;
  private JTextField ticketTemplateNameTextField;
  private JEditorPane ticketTemplateContentEditorPane;
  private JEditorPane scpWithSelectionEditorPane;
  private JEditorPane scpWithoutSelectionEditorPane;
  private JButton newButton;
  private JButton removeButton;

  private DefaultComboBoxModel componentComboBoxModel;
  private DefaultComboBoxModel priorityComboBoxModel;
  private DefaultComboBoxModel typeComboBoxModel;
  private DefaultComboBoxModel milestoneComboBoxModel;
  private DefaultComboBoxModel versionComboBoxModel;

  private SimpleListModel<Template> ticketTemplatesListModel;

  public ConfigurationForm() {
    testConnectionButton.addActionListener(new ActionListener() {
      /**
       * Invoked when an action occurs.
       */
      public void actionPerformed(ActionEvent e) {
        Container parent = ((Component) e.getSource()).getParent();
        Cursor oldCursor = parent.getCursor();
        parent.setCursor(MouseCursors.WAIT_CURSOR);
        ResourceBundle bundle = BundleLocator.getBundle();
        TracGateway gateway = TracGatewayLocator.retrieveTracGateway();
        try {
          ConnectionSettings settings = new ConnectionSettings();
          settings.setLogin(login.getText());
          settings.setPassword(password.getText());
          settings.setTracUrl(tracUrl.getText());
          gateway.testConnection(settings);
          Messages.showMessageDialog(bundle.getString("configuration.connection.dialogs.connection_success"), bundle.getString("dialogs.success"), Messages.getInformationIcon());
        } catch (ConnectionFailedException exception) {
          Messages.showMessageDialog(bundle.getString("configuration.connection.dialogs.connection_failed"), bundle.getString("dialogs.error"), Messages.getErrorIcon());
        } catch (MalformedURLException e1) {
          Messages.showMessageDialog(bundle.getString("configuration.connection.dialogs.malformed_url"), bundle.getString("dialogs.error"), Messages.getErrorIcon());
        } finally {
          parent.setCursor(oldCursor);
        }
      }
    });
    ticketTemplatesList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        ListSelectionModel model = (ListSelectionModel) e.getSource();
        if (model.isSelectionEmpty()) {
          ticketTemplateNameTextField.setEnabled(false);
          ticketTemplateContentEditorPane.setEnabled(false);
          ticketTemplateNameTextField.setText(null);
          ticketTemplateContentEditorPane.setText(null);
          removeButton.setEnabled(false);
        } else {
          Template selected = (Template) ticketTemplatesList.getSelectedValue();
          ticketTemplateNameTextField.setEnabled(true);
          ticketTemplateContentEditorPane.setEnabled(true);
          ticketTemplateNameTextField.setText(selected.getName());
          ticketTemplateContentEditorPane.setText(selected.getContent());
          removeButton.setEnabled(true);
        }

      }
    });
    ticketTemplateNameTextField.getDocument().addDocumentListener(new DocumentAdapter() {
      protected void textChanged(DocumentEvent e) {
        Template selected = (Template) ticketTemplatesList.getSelectedValue();
        if (selected != null) {
          selected.setName(ticketTemplateNameTextField.getText());
        }
      }
    });
    ticketTemplateContentEditorPane.getDocument().addDocumentListener(new DocumentAdapter() {
      protected void textChanged(DocumentEvent e) {
        Template selected = (Template) ticketTemplatesList.getSelectedValue();
        if (selected != null) {
          selected.setContent(ticketTemplateContentEditorPane.getText());
        }
      }
    });
    newButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Template template = new Template();
        template.setName(BundleLocator.getBundle().getString("configuration.ticket_templates.new_template_name"));
        ticketTemplatesListModel.add(template);
      }
    });
    removeButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ticketTemplatesListModel.remove(ticketTemplatesList.getSelectedIndex());
      }
    });
  }


  public void setData(TracConfiguration data) {
    ConnectionSettings connectionSettings = data.getConnectionSettings();
    if (connectionSettings != null) {
      tracUrl.setText(connectionSettings.getTracUrl());
      login.setText(connectionSettings.getLogin());
      password.setText(connectionSettings.getPassword());
    }

    DefaultValues defaultValues = data.getDefaultValues();
    if (defaultValues != null) {
      componentComboBoxModel.setSelectedItem(defaultValues.getComponent());
      priorityComboBoxModel.setSelectedItem(defaultValues.getPriority());
      typeComboBoxModel.setSelectedItem(defaultValues.getType());
      milestoneComboBoxModel.setSelectedItem(defaultValues.getMilestone());
      versionComboBoxModel.setSelectedItem(defaultValues.getVersion());

      try {
        fillComboBox(componentComboBoxModel, gateway.retrieveComponents(), data.getDefaultValues().getComponent(), false);
        fillComboBox(priorityComboBoxModel, gateway.retrievePriorities(), data.getDefaultValues().getPriority(), false);
        fillComboBox(typeComboBoxModel, gateway.retrieveTypes(), data.getDefaultValues().getType(), false);
        fillComboBox(milestoneComboBoxModel, gateway.retrieveMilestones(), data.getDefaultValues().getMilestone(), false);
        fillComboBox(versionComboBoxModel, gateway.retrieveVersions(), data.getDefaultValues().getVersion(), false);
      } catch (ConnectionFailedException e) {
        //nothing to do
      }
    }

    if (data.getTicketTemplates() != null) {
      ticketTemplatesListModel.setData(DeepCopyUtils.deepCopy(data.getTicketTemplates()));
    }

    SendCodePointerSettings scpSettings = data.getSendCodePointerSettings();
    if (scpSettings != null) {
      scpWithoutSelectionEditorPane.setText(scpSettings.getNoSelectionTemplate().getContent());
      scpWithSelectionEditorPane.setText(scpSettings.getSelectionTemplate().getContent());
    }

  }

  public void getData(TracConfiguration data) {
    ConnectionSettings connectionSettings = new ConnectionSettings();
    connectionSettings.setTracUrl(tracUrl.getText());
    connectionSettings.setLogin(login.getText());
    connectionSettings.setPassword(password.getText());
    data.setConnectionSettings(connectionSettings);

    DefaultValues defaultValues = new DefaultValues();
    defaultValues.setComponent((String) componentComboBoxModel.getSelectedItem());
    defaultValues.setMilestone((String) milestoneComboBoxModel.getSelectedItem());
    defaultValues.setPriority((String) priorityComboBoxModel.getSelectedItem());
    defaultValues.setType((String) typeComboBoxModel.getSelectedItem());
    defaultValues.setVersion((String) versionComboBoxModel.getSelectedItem());
    data.setDefaultValues(defaultValues);

    SendCodePointerSettings sendCodePointerSettings = new SendCodePointerSettings();
    sendCodePointerSettings.setNoSelectionTemplate(new Template(scpWithoutSelectionEditorPane.getText()));
    sendCodePointerSettings.setSelectionTemplate(new Template(scpWithSelectionEditorPane.getText()));
    data.setSendCodePointerSettings(sendCodePointerSettings);
    data.setTicketTemplates(ticketTemplatesListModel.getList());
  }

  public boolean isModified(TracConfiguration data) {
    ConnectionSettings connectionSettings = data.getConnectionSettings();
    DefaultValues defaultValues = data.getDefaultValues();
    java.util.List<Template> templates = data.getTicketTemplates();
    SendCodePointerSettings scpSettings = data.getSendCodePointerSettings();

    boolean connectionSettingsModified = false;
    if (connectionSettings != null) {
      connectionSettingsModified = (!login.getText().equals(connectionSettings.getLogin()) ||
        !tracUrl.getText().equals(connectionSettings.getTracUrl()) ||
        !password.getText().equals(connectionSettings.getPassword()));
    }

    boolean defaultValuesModified = false;
    if (defaultValues != null) {
      defaultValuesModified = modified(componentComboBoxModel, defaultValues.getComponent()) ||
        modified(milestoneComboBoxModel, defaultValues.getMilestone()) ||
        modified(versionComboBoxModel, defaultValues.getVersion()) ||
        modified(typeComboBoxModel, defaultValues.getType()) ||
        modified(priorityComboBoxModel, defaultValues.getPriority());
    }

    boolean templatesModified = false;
    if (templates != null) {
      templatesModified = !CollectionsUtils.areListsEqual(ticketTemplatesListModel.getList(), templates);
    }

    boolean scpSettingsModified = false;
    if (scpSettings != null) {
      Template inEditorWithout = new Template(scpWithoutSelectionEditorPane.getText());
      Template inEditorWith = new Template(scpWithSelectionEditorPane.getText());
      scpSettingsModified = !inEditorWith.equals(scpSettings.getSelectionTemplate()) ||
        !inEditorWithout.equals(scpSettings.getNoSelectionTemplate());
    }

    return connectionSettingsModified || defaultValuesModified || templatesModified || scpSettingsModified;
  }

  private boolean modified(ComboBoxModel model, String value) {
    boolean result;
    Object selected = model.getSelectedItem();
    if (selected == null) {
      result = value != null;
    } else {
      result = !selected.equals(value);
    }
    return result;
  }

  public JTabbedPane getRootComponent() {
    return rootComponent;
  }

  private void createUIComponents() {
    componentComboBoxModel = new DefaultComboBoxModel();
    priorityComboBoxModel = new DefaultComboBoxModel();
    typeComboBoxModel = new DefaultComboBoxModel();
    milestoneComboBoxModel = new DefaultComboBoxModel();
    versionComboBoxModel = new DefaultComboBoxModel();

    ticketTemplatesListModel = new SimpleListModel<Template>();

    componentComboBox = new JComboBox(componentComboBoxModel);
    priorityComboBox = new JComboBox(priorityComboBoxModel);
    typeComboBox = new JComboBox(typeComboBoxModel);
    milestoneComboBox = new JComboBox(milestoneComboBoxModel);
    versionComboBox = new JComboBox(versionComboBoxModel);
    ticketTemplatesList = new JList(ticketTemplatesListModel);
    ticketTemplatesList.setCellRenderer(new TicketTemplatesListCellRenderer());

  }
}