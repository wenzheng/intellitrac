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

package org.trzcinka.intellitrac.view.toolwindow.wiki;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.ui.Messages;
import org.apache.log4j.Logger;
import org.trzcinka.intellitrac.BundleLocator;
import org.trzcinka.intellitrac.model.ApplicationModel;
import org.trzcinka.intellitrac.model.IntelliTracConfiguration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

/**
 * todo class description
 * <p/>
 * Created on: 2008-12-09 01:48:39 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Michal Trzcinka
 */

public class WikiTabForm {
  private static final Logger log = Logger.getLogger(WikiTabForm.class.getName());
  private JButton launchWikiButton;
  private JPanel rootComponent;

  public WikiTabForm() {
    launchWikiButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (BrowserUtil.canStartDefaultBrowser()) {
          String baseUrl = ApplicationModel.getConnectionSettings().getTracUrl();
          String suffix = IntelliTracConfiguration.getInstance().getConfiguration().getString("wiki_suffix");
          String url = baseUrl + suffix;
          BrowserUtil.launchBrowser(url);
        } else {
          ResourceBundle bundle = BundleLocator.getBundle();
          Messages.showWarningDialog(ApplicationModel.getProject(), bundle.getString("dialogs.cannot_start_browser"), bundle.getString("dialogs.warning"));
        }
      }
    });
  }

  public Component getRootComponent() {
    return rootComponent;
  }

}