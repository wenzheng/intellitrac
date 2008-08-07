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

package org.trzcinka.intellitrac.model;

/**
 * Whenever GUI state changes, this class provides additional information about it. For example, if
 * a user decided to edit a ticket, the new StateInfo instance should be created with
 * TICKET_EDITOR as state and additional data in provided object (for example ticket ID to edit
 * a ticket).
 */
public class TicketsStateInfo {

  private TicketsState ticketsState;
  private Object info;

  /**
   * Instantiates state info.
   *
   * @param ticketsState new state.
   * @param info         info, may be null.
   */
  public TicketsStateInfo(TicketsState ticketsState, Object info) {
    this.ticketsState = ticketsState;
    this.info = info;
  }

  public TicketsState getState() {
    return ticketsState;
  }

  public Object getInfo() {
    return info;
  }

}