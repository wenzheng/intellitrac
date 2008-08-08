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

import org.trzcinka.intellitrac.model.tickets.TicketsState;
import org.trzcinka.intellitrac.model.tickets.TicketsStateChangeListener;
import org.trzcinka.intellitrac.view.toolwindow.tickets.report_editor.ReportEditorForm;
import org.trzcinka.intellitrac.view.toolwindow.tickets.reports_list.ReportsListForm;
import org.trzcinka.intellitrac.view.toolwindow.tickets.ticket_editor.TicketEditorForm;
import org.trzcinka.intellitrac.view.toolwindow.tickets.tickets_list.TicketsListForm;

import javax.swing.*;
import java.awt.*;

public class TicketsTabForm extends BaseTicketsForm implements TicketsStateChangeListener {

  private CardLayout cardLayout;
  private JPanel rootComponent;

  private ReportsListForm reportsListForm;
  private ReportEditorForm reportEditorForm;
  private TicketsListForm ticketsListForm;
  private TicketEditorForm ticketEditorForm;

  public TicketsTabForm() {
    ticketsModel.addStateListener(this);
  }

  public JComponent getRootComponent() {
    return rootComponent;
  }

  private void createUIComponents() {
    cardLayout = new CardLayout();
    rootComponent = new JPanel(cardLayout);

    reportsListForm = new ReportsListForm();
    reportEditorForm = new ReportEditorForm();
    ticketsListForm = new TicketsListForm();
    ticketEditorForm = new TicketEditorForm();

    rootComponent.add(TicketsState.REPORTS_LIST.toString(), reportsListForm.getRootComponent());
    rootComponent.add(TicketsState.REPORT_EDITOR.toString(), reportEditorForm.getRootComponent());
    rootComponent.add(TicketsState.TICKETS_LIST.toString(), ticketsListForm.getRootComponent());
    rootComponent.add(TicketsState.TICKET_EDITOR.toString(), ticketEditorForm.getRootComponent());
  }

  public void stateChanged(TicketsState state) {
    cardLayout.show(rootComponent, state.toString());
  }

}