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
import org.trzcinka.intellitrac.components.ConfigurationComponent;
import org.trzcinka.intellitrac.dto.TicketChange;
import org.trzcinka.intellitrac.model.ApplicationModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TicketChangesHistoryPopup extends JDialog {
  private static final ListCellRenderer CELL_RENDERER = new TicketChangesListCellRenderer();

  private static String tracUrl = ApplicationModel.getInstance().getProject().getComponent(ConfigurationComponent.class).getConnectionSettings().getTracUrl();
  private static IWikiModel tracModel = new TracModel(tracUrl, tracUrl);

  private JPanel contentPane;
  private JButton buttonOK;
  private JList changesList;
  private JEditorPane commentPane;
  private DefaultListModel changesListModel;
  private List<TicketChange> changes;


  public TicketChangesHistoryPopup(Window window, List<TicketChange> changes) {
    super(window);
    this.changes = changes;
    setContentPane(contentPane);
    setModal(true);
    getRootPane().setDefaultButton(buttonOK);

    buttonOK.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        onOK();
      }
    });

    changesList.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        TicketChange ticketChange = (TicketChange) changesList.getSelectedValue();
        if (ticketChange.getField().equals(TicketChange.COMMENT)) {
          String value = ticketChange.getNewValue();
          commentPane.setText(tracModel.render(value));
        } else {
          commentPane.setText(null);
        }
      }
    });
  }

  private void onOK() {
    dispose();
  }

  private void createUIComponents() {
    changesListModel = new DefaultListModel();
    changesList = new JList(changesListModel);
    changesListModel.removeAllElements();
    for (TicketChange ticketChange : changes) {
      changesListModel.addElement(ticketChange);
    }
    changesList.setCellRenderer(CELL_RENDERER);
  }
}
