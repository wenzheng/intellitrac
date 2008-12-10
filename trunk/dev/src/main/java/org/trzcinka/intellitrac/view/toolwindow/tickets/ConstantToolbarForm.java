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

import org.trzcinka.intellitrac.dto.Template;
import org.trzcinka.intellitrac.dto.Ticket;
import org.trzcinka.intellitrac.model.ApplicationModel;
import org.trzcinka.intellitrac.model.tickets.State;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ConstantToolbarForm extends BaseTicketsForm implements ActionListener {

  private JPanel rootComponent;

  private JToolBar toolbar;
  private JButton newTicket;
  private JButton goHome;
  private JButton goBack;

  public ConstantToolbarForm() {
    newTicket.addActionListener(this);
    goBack.addActionListener(this);
    goHome.addActionListener(this);
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == goHome) {
      ticketsModel.setCurrentState(State.REPORTS_LIST);
    } else if (e.getSource() == goBack) {
      ticketsModel.goBack();
    } else if (e.getSource() == newTicket) {
      List<Template> ticketTemplates = ApplicationModel.getTicketTemplates();
      if (ticketTemplates.isEmpty()) {
        ticketsModel.getCurrentTicketModel().setCurrentTicket(new Ticket());
        ticketsModel.setCurrentState(State.TICKET_CREATOR);
      } else if (ticketTemplates.size() == 1) {
        ticketsModel.getCurrentTicketModel().setCurrentTicket(new Ticket(ticketTemplates.get(0).getContent()));
        ticketsModel.setCurrentState(State.TICKET_CREATOR);
      } else {
        showTemplatesMenu(ticketTemplates);
      }

    }
  }

  private void showTemplatesMenu(Iterable<Template> ticketTemplates) {
    JPopupMenu popup = new JPopupMenu();
    for (final Template template : ticketTemplates) {
      JMenuItem menuItem = new JMenuItem(template.getName());
      menuItem.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          ticketsModel.getCurrentTicketModel().setCurrentTicket(new Ticket(template.getContent()));
          ticketsModel.setCurrentState(State.TICKET_CREATOR);
        }
      });
      popup.add(menuItem);
    }
    popup.show(newTicket, newTicket.getX(), newTicket.getY());
  }

}