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

package org.trzcinka.intellitrac.model.tickets.tickets_list;

import org.trzcinka.intellitrac.dto.Ticket;

/**
 * Represents single column in tickets list.
 */
public interface TicketsListColumn {

  /**
   * Retrieves proper property of a ticket.
   *
   * @param ticket ticket. Not null.
   * @return property of a ticket.
   */
  Object getValue(Ticket ticket);

  /**
   * Retrieves this column class.
   *
   * @return Retrieves this column class.
   */
  Class<?> getColumnClass();

  /**
   * Retrieves column name.
   *
   * @return Retrieves column name.
   */
  String getColumnName();

}