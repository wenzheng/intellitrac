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

package org.trzcinka.intellitrac.view.toolwindow.tickets.reports_list;

import org.trzcinka.intellitrac.dto.Report;

import javax.swing.*;
import java.awt.*;

/**
 * Is responsible for rendering a single item in reports list.
 */
public class ReportsListCellRenderer extends DefaultListCellRenderer {

  /**
   * {@inheritDoc}
   */
  public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
    if (!(value instanceof Report)) {
      throw new IllegalArgumentException("ReportsListCellRenderer may render cells for Report instances only.");
    }
    Report report = (Report) value;
    return super.getListCellRendererComponent(list, report.getName(), index, isSelected, cellHasFocus);
  }
}