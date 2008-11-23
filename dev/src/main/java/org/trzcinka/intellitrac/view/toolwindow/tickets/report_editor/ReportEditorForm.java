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

package org.trzcinka.intellitrac.view.toolwindow.tickets.report_editor;

import com.intellij.openapi.ui.Messages;
import org.trzcinka.intellitrac.dto.Report;
import org.trzcinka.intellitrac.gateway.ConnectionFailedException;
import org.trzcinka.intellitrac.gateway.TracError;
import org.trzcinka.intellitrac.gateway.TracGatewayLocator;
import org.trzcinka.intellitrac.model.tickets.CurrentReportListener;
import org.trzcinka.intellitrac.model.tickets.State;
import org.trzcinka.intellitrac.view.toolwindow.tickets.BaseTicketsForm;
import org.trzcinka.intellitrac.view.validation.GroupValidator;
import org.trzcinka.intellitrac.view.validation.NotEmptyValidator;
import org.trzcinka.intellitrac.view.validation.Validator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class ReportEditorForm extends BaseTicketsForm implements CurrentReportListener, GroupValidator {

  private JPanel rootComponent;

  private Long id;
  private JTextField name;
  private JTextField description;
  private JTextArea query;

  private JButton okButton;
  private JButton cancelButton;

  private Validator nameValidator;
  private Validator queryValidator;
  private Validator descriptionValidator;
  private Map<Validator, Boolean> validators;

  public ReportEditorForm() {
    ticketsModel.getCurrentReportModel().addListener(this);
    okButton.addActionListener(new ActionListener() {
      /**
       * Invoked when an action occurs.
       */
      public void actionPerformed(ActionEvent e) {
        Report report = new Report();
        getData(report);

        try {
          TracGatewayLocator.retrieveTracGateway().retrieveTickets(report.getProperQuery());
        } catch (TracError er) {
          Messages.showMessageDialog(MessageFormat.format(bundle.getString("tool_window.tickets.report_editor.validation.custom.query"), er.getMessage()), bundle.getString("dialogs.error"), Messages.getErrorIcon());
          return;
        }
        catch (ConnectionFailedException e1) {
          TracGatewayLocator.handleConnectionProblem();
          return;
        }

        ticketsModel.getReportsListModel().saveReport(report);
        reportsListRedirect();
      }
    });
    cancelButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        reportsListRedirect();
      }
    });
  }

  private void disposeValidators() {
    nameValidator.validationDisposed();
    queryValidator.validationDisposed();
    descriptionValidator.validationDisposed();
  }

  private void reportsListRedirect() {
    disposeValidators();
    unsetInputVerifiers();
    State state = State.REPORTS_LIST;
    ticketsModel.setCurrentState(state);
  }

  private void setData(Report data) {
    id = data.getId();
    name.setText(data.getName());
    description.setText(data.getDescription());
    query.setText(data.getQuery());
  }

  private void getData(Report data) {
    data.setId(id);
    data.setName(name.getText());
    data.setDescription(description.getText());
    data.setQuery(query.getText());
  }

  public JComponent getRootComponent() {
    return rootComponent;
  }

  public void currentReportChanged(Report report) {
    setData(report);
    setInputVerifiers();
    if (report.isNew()) {
      falsifyValidators();
    } else {
      truifyValidators();
    }
  }

  private void createUIComponents() {
    name = new JTextField();
    nameValidator = new NotEmptyValidator(this, name, bundle.getString("tool_window.tickets.report_editor.validation.not_empty.name"));


    query = new JTextArea();
    queryValidator = new NotEmptyValidator(this, query, bundle.getString("tool_window.tickets.report_editor.validation.not_empty.query"));

    description = new JTextField();
    descriptionValidator = new NotEmptyValidator(this, description, bundle.getString("tool_window.tickets.report_editor.validation.not_empty.description"));

    setInputVerifiers();

    validators = new HashMap<Validator, Boolean>();
    okButton = new JButton();

    falsifyValidators();
  }

  private void setInputVerifiers() {
    name.setInputVerifier(nameValidator);
    query.setInputVerifier(queryValidator);
    description.setInputVerifier(descriptionValidator);
  }

  private void unsetInputVerifiers() {
    name.setInputVerifier(null);
    query.setInputVerifier(null);
    description.setInputVerifier(null);
  }

  private void falsifyValidators() {
    validators.put(nameValidator, false);
    validators.put(queryValidator, false);
    validators.put(descriptionValidator, false);
    okButton.setEnabled(false);
  }

  private void truifyValidators() {
    validators.put(nameValidator, true);
    validators.put(queryValidator, true);
    validators.put(descriptionValidator, true);
    okButton.setEnabled(true);
  }

  public void validationFailed(Validator validator) {
    validators.put(validator, false);
    checkValidation();
  }

  private void checkValidation() {
    boolean ok = true;
    for (Validator validator : validators.keySet()) {
      if (!validators.get(validator)) {
        ok = false;
      }
    }
    if (ok) {
      okButton.setEnabled(true);
    } else {
      okButton.setEnabled(false);
    }
  }

  public void validationSucceeded(Validator validator) {
    validators.put(validator, true);
    checkValidation();
  }
}