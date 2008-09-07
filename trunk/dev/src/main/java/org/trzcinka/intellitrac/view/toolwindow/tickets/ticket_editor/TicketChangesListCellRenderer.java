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

import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.model.TracModel;
import org.trzcinka.intellitrac.BundleLocator;
import org.trzcinka.intellitrac.components.ConfigurationComponent;
import org.trzcinka.intellitrac.dto.TicketChange;
import org.trzcinka.intellitrac.model.ApplicationModel;
import org.trzcinka.intellitrac.utils.MimeTypes;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class TicketChangesListCellRenderer extends JEditorPane implements ListCellRenderer {

  private static String tracUrl = ApplicationModel.getInstance().getProject().getComponent(ConfigurationComponent.class).getTracUrl();

  private static IWikiModel tracModel = new TracModel(tracUrl, tracUrl);

  public TicketChangesListCellRenderer() {
    setContentType(MimeTypes.TEXT_HTML);
    setEditable(false);
    putClientProperty(HONOR_DISPLAY_PROPERTIES, true);
  }

  public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
    if (!(value instanceof TicketChange)) {
      throw new IllegalArgumentException("value must be TicketChange instance");
    }
    TicketChange change = (TicketChange) value;

    ResourceBundle bundle = BundleLocator.getBundle();
    DateFormat format = new SimpleDateFormat(bundle.getString("tool_window.tickets.ticket_editor.change_history.date_format"));
    StringBuilder text = new StringBuilder(MessageFormat.format(bundle.getString("tool_window.tickets.ticket_editor.change_history.change_header"), format.format(change.getTime()), change.getAuthor()));
    if (TicketChange.COMMENT.equals(change.getField())) {
      text.append(bundle.getString("tool_window.tickets.ticket_editor.change_history.added_comment"));
      text.append(tracModel.render(change.getNewValue()));
    } else {
      text.append(MessageFormat.format(bundle.getString("tool_window.tickets.ticket_editor.change_history.change_info"), change.getField(), change.getOldValue(), change.getNewValue()));
    }

    setText(text.toString());

    setBorder(BorderFactory.createEtchedBorder());
    return this;
  }

}