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

package org.trzcinka.intellitrac.view.toolwindow.tickets;

import org.trzcinka.intellitrac.model.tickets.State;
import org.trzcinka.intellitrac.model.tickets.TicketsStateChangeListener;
import org.trzcinka.intellitrac.view.toolwindow.tickets.report_editor.ReportEditorForm;
import org.trzcinka.intellitrac.view.toolwindow.tickets.reports_list.ReportsListForm;
import org.trzcinka.intellitrac.view.toolwindow.tickets.ticket_editor.TicketCreatorForm;
import org.trzcinka.intellitrac.view.toolwindow.tickets.ticket_editor.TicketEditorForm;
import org.trzcinka.intellitrac.view.toolwindow.tickets.tickets_list.TicketsListForm;

import javax.swing.*;
import java.awt.*;

public class TicketsTabForm extends BaseTicketsForm implements TicketsStateChangeListener {

  private CardLayout cardLayout;
  private JPanel rootComponent;
  private JLabel headerLabel;
  private JPanel contentPanel;

  private ReportsListForm reportsListForm;
  private ReportEditorForm reportEditorForm;
  private TicketsListForm ticketsListForm;
  private TicketEditorForm ticketEditorForm;
  private TicketCreatorForm ticketCreatorForm;

  public TicketsTabForm() {
    ticketsModel.addStateListener(this);
  }

  public JComponent getRootComponent() {
    return rootComponent;
  }

  private void createUIComponents() {
    cardLayout = new CardLayout();
    contentPanel = new JPanel(cardLayout);

    reportsListForm = new ReportsListForm();
    reportEditorForm = new ReportEditorForm();
    ticketsListForm = new TicketsListForm();
    ticketEditorForm = new TicketEditorForm();
    ticketCreatorForm = new TicketCreatorForm();

    contentPanel.add(State.REPORTS_LIST.toString(), reportsListForm.getRootComponent());
    contentPanel.add(State.REPORT_EDITOR.toString(), reportEditorForm.getRootComponent());
    contentPanel.add(State.TICKETS_LIST.toString(), ticketsListForm.getRootComponent());
    contentPanel.add(State.TICKET_EDITOR.toString(), ticketEditorForm.getRootComponent());
    contentPanel.add(State.TICKET_CREATOR.toString(), ticketCreatorForm.getRootComponent());
  }

  public void stateChanged(State state) {
    cardLayout.show(contentPanel, state.toString());
    String headerKey = "tool_window.tickets.header." + state.toString().toLowerCase();
    headerLabel.setText(bundle.getString(headerKey));
  }

  public void appendTextToComment(String text) {
    if (ticketsModel.getCurrentState().equals(State.TICKET_CREATOR)) {
      ticketCreatorForm.appendTextToComment(text);
    } else if (ticketsModel.getCurrentState().equals(State.TICKET_EDITOR)) {
      ticketEditorForm.appendTextToComment(text);
    }
  }

  public void appendTextToDescription(String text) {
    if (ticketsModel.getCurrentState().equals(State.TICKET_CREATOR)) {
      ticketCreatorForm.appendTextToDescription(text);
    } else if (ticketsModel.getCurrentState().equals(State.TICKET_EDITOR)) {
      ticketEditorForm.appendTextToDescription(text);
    }
  }
}