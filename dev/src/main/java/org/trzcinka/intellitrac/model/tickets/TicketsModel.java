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
import org.trzcinka.intellitrac.model.tickets.reports_list.ReportListModel;
import org.trzcinka.intellitrac.model.tickets.tickets_list.TicketsListModel;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents model for tickets tab in tool window.
 */
public class TicketsModel {

  private static TicketsModel instance;

  private TicketsStateInfo currentTicketsState;
  private TicketsStateInfo lastTicketsState;

  private ReportListModel reportListModel;
  private TicketsListModel ticketsListModel;

  private Collection<TicketsStateChangeListener> ticketsStateChangeListeners;

  private TicketsModel() {
    ticketsStateChangeListeners = new ArrayList<TicketsStateChangeListener>();
    currentTicketsState = new TicketsStateInfo(TicketsState.REPORTS_LIST, null);

    ticketsListModel = new TicketsListModel(new ArrayList<Ticket>(0));
    reportListModel = new ReportListModel();
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
   * @param currentTicketsState not null state.
   */
  public void setCurrentState(TicketsStateInfo currentTicketsState) {
    lastTicketsState = this.currentTicketsState;
    this.currentTicketsState = currentTicketsState;
    notifyListeners(currentTicketsState);
  }

  /**
   * Returns previous state or does nothing if there is no previous state. Notifies its listeners about the change.
   */
  public void goBack() {
    currentTicketsState = lastTicketsState;
    if (lastTicketsState != null) {
      setCurrentState(lastTicketsState);
    }
    lastTicketsState = null;
  }

  public TicketsListModel getTicketsListTableModel() {
    return ticketsListModel;
  }

  public ReportListModel getReportsListModel() {
    return reportListModel;
  }

  private void notifyListeners(TicketsStateInfo ticketsStateInfo) {
    for (TicketsStateChangeListener ticketsStateChangeListener : ticketsStateChangeListeners) {
      ticketsStateChangeListener.stateChanged(ticketsStateInfo);
    }
  }

  /**
   * Adds given object to the list of listeners. Listeners will be notified about state changes.
   *
   * @param changeListenerTickets not null listener.
   */
  public void addStateListener(TicketsStateChangeListener changeListenerTickets) {
    ticketsStateChangeListeners.add(changeListenerTickets);
  }

}