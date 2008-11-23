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

import org.apache.log4j.Logger;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Model for filter columns.
 * <p/>
 * Created on: 2008-11-23 18:40:43 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Michal Trzcinka
 */

public class FilterColumnsComboModel extends AbstractListModel implements ComboBoxModel {
  private static final Logger log = Logger.getLogger(FilterColumnsComboModel.class.getName());

  private List<TicketsListColumn> columns = new ArrayList<TicketsListColumn>();
  private TicketsListColumn selected;

  public FilterColumnsComboModel() {
    columns = new ArrayList<TicketsListColumn>();
    //note: the order of elements in this list must match proper constants in TicketsListModel
    columns.add(new TicketNumberColumn());
    columns.add(new TicketSummaryColumn());
    columns.add(new TicketTypeColumn());
    columns.add(new TicketOwnerColumn());
    columns.add(new TicketStatusColumn());
  }

  public int getSize() {
    return columns.size();
  }

  public Object getElementAt(int index) {
    return columns.get(index);
  }

  public void setSelectedItem(Object anItem) {
    selected = (TicketsListColumn) anItem;
  }

  public Object getSelectedItem() {
    return selected;
  }

}