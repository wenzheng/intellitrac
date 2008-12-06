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

package org.trzcinka.intellitrac.view.configuration;

import org.apache.log4j.Logger;
import org.trzcinka.intellitrac.dto.Template;

import javax.swing.*;
import java.awt.*;

/**
 * Responsible for rendering templates list cells.
 * <p/>
 *
 * @author Michal Trzcinka
 */

public class TicketTemplatesListCellRenderer extends DefaultListCellRenderer {
  private static final Logger log = Logger.getLogger(TicketTemplatesListCellRenderer.class.getName());

  public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
    if (!(value instanceof Template)) {
      throw new IllegalArgumentException();
    }
    Template template = (Template) value;
    return super.getListCellRendererComponent(list, template.getName(), index, isSelected, cellHasFocus);
  }

}