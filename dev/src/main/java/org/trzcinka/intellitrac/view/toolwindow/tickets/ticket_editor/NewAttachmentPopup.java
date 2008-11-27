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

import org.apache.commons.lang.StringUtils;
import org.trzcinka.intellitrac.gateway.ConnectionFailedException;
import org.trzcinka.intellitrac.gateway.TracGatewayLocator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class NewAttachmentPopup extends JDialog {
  private JPanel contentPane;
  private JButton buttonOK;
  private JButton buttonCancel;
  private JTextPane description;
  private JButton selectFileButton;
  private JLabel fileName;
  private JCheckBox replace;

  private File selectedFile;
  private Integer ticketId;

  public NewAttachmentPopup(Window parent, Integer id) {
    super(parent);
    ticketId = id;
    setContentPane(contentPane);
    setModal(true);
    getRootPane().setDefaultButton(buttonOK);

    buttonOK.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        onOK();
      }
    });

    buttonCancel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        onCancel();
      }
    });

// call onCancel() when cross is clicked
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onCancel();
      }
    });

// call onCancel() on ESCAPE
    contentPane.registerKeyboardAction(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        onCancel();
      }
    }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    selectFileButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(false);
        int ret = fc.showOpenDialog(contentPane);
        if (ret == JFileChooser.APPROVE_OPTION) {
          selectedFile = fc.getSelectedFile();
          fileName.setText(selectedFile.getName());
          buttonOK.setEnabled(true);
          pack();
        }
      }
    });
  }

  private void onOK() {
    try {
      TracGatewayLocator.retrieveTracGateway().saveAttachment(ticketId, selectedFile, description.getText(), replace.isSelected());
    } catch (ConnectionFailedException e) {
      TracGatewayLocator.handleConnectionProblem();
    }
    dispose();
  }

  private void onCancel() {
    selectedFile = null;
    fileName.setText(StringUtils.EMPTY);
    buttonOK.setEnabled(false);
    dispose();
  }

}
