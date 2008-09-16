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

import org.trzcinka.intellitrac.dto.Ticket;
import org.trzcinka.intellitrac.model.tickets.State;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConstantToolbarForm extends BaseTicketsForm {

  private JPanel rootComponent;

  private JToolBar toolbar;
  private JButton newTicket;
  private JButton goHome;
  private JButton goBack;

  public ConstantToolbarForm() {
    goHome.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ticketsModel.setCurrentState(State.REPORTS_LIST);
      }
    });
    goBack.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ticketsModel.goBack();
      }
    });
    newTicket.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ticketsModel.getCurrentTicketModel().setCurrentTicket(new Ticket());
        ticketsModel.setCurrentState(State.TICKET_CREATOR);
      }
    });
  }
}