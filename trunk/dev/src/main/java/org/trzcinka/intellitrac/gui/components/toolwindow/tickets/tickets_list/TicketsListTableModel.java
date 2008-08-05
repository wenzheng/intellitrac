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

package org.trzcinka.intellitrac.gui.components.toolwindow.tickets.tickets_list;

import org.trzcinka.intellitrac.dto.Ticket;

import javax.swing.table.AbstractTableModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Table model for tickets list.
 */
public class TicketsListTableModel extends AbstractTableModel {
  private static final int COLUMN_COUNT = 5;

  private static final int COLUMN_NUMBER = 0;
  private static final int COLUMN_SUMMARY = 1;
  private static final int COLUMN_TYPE = 2;
  private static final int COLUMN_OWNER = 3;
  private static final int COLUMN_STATUS = 4;

  private List<Ticket> tickets;

  private Map<Integer, TicketsListColumn> columns;

  /**
   * Instantiates new TicketsListTableModel with given tickets list.
   *
   * @param tickets not null.
   */
  public TicketsListTableModel(List<Ticket> tickets) {
    this.tickets = tickets;
    setUpColumns();

  }

  private void setUpColumns() {
    columns = new HashMap<Integer, TicketsListColumn>(COLUMN_COUNT);
    columns.put(COLUMN_NUMBER, new TicketNumberColumn());
    columns.put(COLUMN_SUMMARY, new TicketSummaryColumn());
    columns.put(COLUMN_TYPE, new TicketTypeColumn());
    columns.put(COLUMN_OWNER, new TicketOwnerColumn());
    columns.put(COLUMN_STATUS, new TicketStatusColumn());
  }

  public int getRowCount() {
    return tickets.size();
  }

  public int getColumnCount() {
    return COLUMN_COUNT;
  }

  public Object getValueAt(int rowIndex, int columnIndex) {
    Ticket ticket = tickets.get(rowIndex);
    return columns.get(columnIndex).getValue(ticket);
  }

  public void updateTickets(List<Ticket> tickets) {
    this.tickets = tickets;
    fireTableDataChanged();
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return columns.get(columnIndex).getColumnClass();
  }

  @Override
  public String getColumnName(int column) {
    return columns.get(column).getColumnName();
  }

  public Ticket getTicket(int selectedRow) {
    return tickets.get(selectedRow);
  }
}