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

package org.trzcinka.intellitrac.view.toolwindow.tickets.tickets_list;

import org.apache.log4j.Logger;
import org.trzcinka.intellitrac.model.tickets.tickets_list.TicketsListColumn;

import javax.swing.*;
import java.awt.*;

/**
 * Renders elements of filter columns combo.
 * <p/>
 * Created on: 2008-11-23 18:52:56 <br/>
 *
 * @author Michal Trzcinka
 */

public class FilterColumnsComboRenderer extends DefaultListCellRenderer {
  private static final Logger log = Logger.getLogger(FilterColumnsComboRenderer.class.getName());

  @Override
  public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
    TicketsListColumn column = (TicketsListColumn) value;
    return super.getListCellRendererComponent(list, column.getColumnName(), index, isSelected, cellHasFocus);
  }
}