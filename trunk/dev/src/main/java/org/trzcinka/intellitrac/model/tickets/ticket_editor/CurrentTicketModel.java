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

package org.trzcinka.intellitrac.model.tickets.ticket_editor;

import org.trzcinka.intellitrac.dto.Ticket;
import org.trzcinka.intellitrac.model.tickets.CurrentTicketListener;

import java.util.ArrayList;
import java.util.Collection;

public class CurrentTicketModel {

  private Collection<CurrentTicketListener> listeners;

  private Ticket currentTicket;

  public CurrentTicketModel() {
    listeners = new ArrayList<CurrentTicketListener>();
  }

  /**
   * Adds given object to the list of listeners. Listeners will be notified about state changes.
   *
   * @param currentTicketListener not null listener.
   */
  public void addListener(CurrentTicketListener currentTicketListener) {
    listeners.add(currentTicketListener);
  }

  private void notifyListeners() {
    for (CurrentTicketListener listener : listeners) {
      listener.currentTicketChanged(currentTicket);
    }
  }

  public Ticket getCurrentTicket() {
    return currentTicket;
  }

  public void setCurrentTicket(Ticket currentTicket) {
    this.currentTicket = currentTicket;
    notifyListeners();
  }

}