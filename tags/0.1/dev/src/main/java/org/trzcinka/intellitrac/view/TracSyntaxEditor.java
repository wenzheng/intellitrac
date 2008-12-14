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

package org.trzcinka.intellitrac.view;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.Messages;
import org.trzcinka.intellitrac.BundleLocator;
import org.trzcinka.intellitrac.gateway.ConnectionFailedException;
import org.trzcinka.intellitrac.gateway.TracGatewayLocator;
import org.trzcinka.intellitrac.utils.MimeTypes;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

/**
 * JEditorPane wrapper that allows to edit using TracWiki syntax and preview.
 * <p/>
 * Created on: 2008-12-07 18:53:25 <br/>
 *
 * @author Michal Trzcinka
 */

public class TracSyntaxEditor {
  private static final Logger log = Logger.getInstance(TracSyntaxEditor.class.getName());

  private JEditorPane editor;
  private JRadioButton previewRadioButton;
  private JRadioButton editRadioButton;
  private JPanel rootComponent;
  private String rawText; //holds 'raw' text (in tracwiki format) while in preview mode
  private boolean preview;

  public TracSyntaxEditor() {
    editRadioButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        setEditMode();
      }
    });
    previewRadioButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        setPreviewMode();
      }
    });
    editor.addHyperlinkListener(new HyperlinkListener() {
      public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
          if (BrowserUtil.canStartDefaultBrowser()) {
            BrowserUtil.launchBrowser(e.getURL().toString());
          } else {
            ResourceBundle bundle = BundleLocator.getBundle();
            Messages.showMessageDialog(bundle.getString("dialogs.cannot_start_browser"), bundle.getString("dialogs.warning"), Messages.getWarningIcon());
          }
        }
      }
    });
  }

  public void setPreviewMode() {
    if (!preview) {
      previewRadioButton.setSelected(true);
      preview = true;

      rawText = editor.getText();
      editor.setContentType(MimeTypes.TEXT_HTML);
      String html = null;
      try {
        html = TracGatewayLocator.retrieveTracGateway().wikiToHtml(rawText);
      } catch (ConnectionFailedException e) {
        TracGatewayLocator.handleConnectionProblem();
      }
      editor.setText(html);
      editor.setEditable(false);
    }
  }

  public void setEditMode() {
    if (preview) {
      editRadioButton.setSelected(true);
      preview = false;
      editor.setContentType(MimeTypes.TEXT_PLAIN);
      editor.setEditable(true);
      editor.setText(rawText);
    }
  }

  public void setText(String text) {
    rawText = text;
    if (preview) {
      try {
        editor.setText(TracGatewayLocator.retrieveTracGateway().wikiToHtml(rawText));
      } catch (ConnectionFailedException e) {
        TracGatewayLocator.handleConnectionProblem();
      }
    } else {
      editor.setText(rawText);
    }
  }

  public String getText() {
    if (preview) {
      return rawText;
    } else {
      return editor.getText();
    }
  }

  public Component getRootComponent() {
    return rootComponent;
  }

}