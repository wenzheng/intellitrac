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

import com.intellij.openapi.ui.Messages;
import org.trzcinka.intellitrac.BundleLocator;
import org.trzcinka.intellitrac.dto.TracConfiguration;
import org.trzcinka.intellitrac.dto.TracConfigurationBean;
import org.trzcinka.intellitrac.gateway.ConnectionFailedException;
import org.trzcinka.intellitrac.gateway.TracGateway;
import org.trzcinka.intellitrac.gateway.TracGatewayLocator;
import org.trzcinka.intellitrac.view.view_utils.MouseCursors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.util.ResourceBundle;

/**
 * IntelliTrac configuration form. The configuration is per-project - it is shown under
 * project settings.
 */
public class ConfigurationForm {

  private JPanel rootComponent;
  private JTextField tracUrl;
  private JTextField login;
  private JTextField password;
  private JButton testConnectionButton;

  public ConfigurationForm() {
    testConnectionButton.addActionListener(new ActionListener() {
      /**
       * Invoked when an action occurs.
       */
      public void actionPerformed(ActionEvent e) {
        Container parent = ((Component) e.getSource()).getParent();
        Cursor oldCursor = parent.getCursor();
        parent.setCursor(MouseCursors.WAIT_CURSOR);
        ResourceBundle bundle = BundleLocator.getBundle();
        TracGateway gateway = TracGatewayLocator.retrieveTracGateway();
        try {
          TracConfiguration configuration = new TracConfigurationBean();
          getData(configuration);
          gateway.testConnection(configuration);
          Messages.showMessageDialog(bundle.getString("configuration.dialogs.connection_success"), bundle.getString("dialogs.success"), Messages.getInformationIcon());
        } catch (ConnectionFailedException exception) {
          Messages.showMessageDialog(bundle.getString("configuration.dialogs.connection_failed"), bundle.getString("dialogs.error"), Messages.getErrorIcon());
        } catch (MalformedURLException e1) {
          Messages.showMessageDialog(bundle.getString("configuration.dialogs.malformed_url"), bundle.getString("dialogs.error"), Messages.getErrorIcon());
        } finally {
          parent.setCursor(oldCursor);
        }
      }
    });
  }

  public JComponent getRootComponent() {
    return rootComponent;
  }

  public void setData(TracConfiguration data) {
    tracUrl.setText(data.getTracUrl());
    login.setText(data.getLogin());
    password.setText(data.getPassword());
  }

  public void getData(TracConfiguration data) {
    data.setTracUrl(tracUrl.getText());
    data.setLogin(login.getText());
    data.setPassword(password.getText());
  }

  public boolean isModified(TracConfiguration data) {
    if (tracUrl.getText() != null ? !tracUrl.getText().equals(data.getTracUrl()) : data.getTracUrl() != null) {
      return true;
    }
    if (login.getText() != null ? !login.getText().equals(data.getLogin()) : data.getLogin() != null) {
      return true;
    }
    if (password.getText() != null ? !password.getText().equals(data.getPassword()) : data.getPassword() != null) {
      return true;
    }
    return false;
  }

}