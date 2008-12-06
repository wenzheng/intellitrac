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

package org.trzcinka.intellitrac.model.tickets;

import org.trzcinka.intellitrac.dto.Ticket;
import org.trzcinka.intellitrac.model.tickets.report_editor.CurrentReportModel;
import org.trzcinka.intellitrac.model.tickets.reports_list.ReportsListModel;
import org.trzcinka.intellitrac.model.tickets.ticket_editor.CurrentTicketModel;
import org.trzcinka.intellitrac.model.tickets.tickets_list.TicketsListModel;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents model for tickets tab in tool window.
 */
public class TicketsModel {

  private static TicketsModel instance;

  private static final State INITIAL_STATE = State.REPORTS_LIST;

  private State currentState;
  private State lastState;

  private CurrentReportModel currentCurrentReportModel;
  private CurrentTicketModel currentTicketModel;
  private ReportsListModel reportsListModel;
  private TicketsListModel ticketsListModel;

  private Collection<TicketsStateChangeListener> ticketsStateChangeListeners;

  private TicketsModel() {
    ticketsStateChangeListeners = new ArrayList<TicketsStateChangeListener>();

    currentState = INITIAL_STATE;

    ticketsListModel = new TicketsListModel(new ArrayList<Ticket>(0));
    reportsListModel = new ReportsListModel();
    currentCurrentReportModel = new CurrentReportModel();
    currentTicketModel = new CurrentTicketModel();
  }

  public static TicketsModel getInstance() {
    if (instance == null) {
      instance = new TicketsModel();
    }
    return instance;
  }

  /**
   * Sets current state. Notifies its listeners about the change.
   *
   * @param currentState not null state.
   */
  public void setCurrentState(State currentState) {
    lastState = this.currentState;
    this.currentState = currentState;
    notifyListeners(currentState);
  }

  public State getCurrentState() {
    return currentState;
  }

  /**
   * Returns previous state or does nothing if there is no previous state. Notifies its listeners about the change.
   */
  public void goBack() {
    currentState = lastState;
    if (lastState != null) {
      setCurrentState(lastState);
    }
    lastState = null;
  }

  public TicketsListModel getTicketsListTableModel() {
    return ticketsListModel;
  }

  public ReportsListModel getReportsListModel() {
    return reportsListModel;
  }

  public CurrentReportModel getCurrentReportModel() {
    return currentCurrentReportModel;
  }

  public CurrentTicketModel getCurrentTicketModel() {
    return currentTicketModel;
  }

  private void notifyListeners(State state) {
    for (TicketsStateChangeListener ticketsStateChangeListener : ticketsStateChangeListeners) {
      ticketsStateChangeListener.stateChanged(state);
    }
  }

  /**
   * Adds given object to the list of listeners. Listeners will be notified about state changes.
   *
   * @param ticketsStateChangeListener not null listener.
   */
  public void addStateListener(TicketsStateChangeListener ticketsStateChangeListener) {
    ticketsStateChangeListeners.add(ticketsStateChangeListener);
  }

}