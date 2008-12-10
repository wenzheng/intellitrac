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

package org.trzcinka.intellitrac.view.toolwindow.tickets.ticket_editor;

import org.apache.commons.io.FileUtils;
import org.trzcinka.intellitrac.BundleLocator;
import org.trzcinka.intellitrac.dto.Attachment;
import org.trzcinka.intellitrac.view.view_utils.ListCellRendererUtils;

import javax.swing.*;
import java.awt.*;
import java.text.MessageFormat;

/**
 * Renders single attachment element.
 * <p/>
 * Created on: 2008-09-20 23:49:29 <br/>
 *
 * @author Michal Trzcinka
 */

public class AttachmentsListCellRenderer implements ListCellRenderer {


  public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

    if (!(value instanceof Attachment)) {
      throw new IllegalArgumentException("AttachmentsListCellRenderer may render only Attachments, not " + value.getClass().getName());
    }
    Attachment attachment = (Attachment) value;

    final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    ListCellRendererUtils.applyDefaultDisplaySettings(list, index, isSelected, cellHasFocus, panel);

    JLabel fileName = new JLabel(attachment.getFileName());
    fileName.setForeground(panel.getForeground());
    panel.add(fileName);

    String additionalTextString = MessageFormat.format(BundleLocator.getBundle().getString("tool_window.tickets.ticket_editor.attachments.attachment_info"), FileUtils.byteCountToDisplaySize(attachment.getSize()), attachment.getAuthor());
    JLabel additionalText = new JLabel(additionalTextString);
    additionalText.setForeground(panel.getForeground());
    panel.add(additionalText);

    return panel;
  }

}