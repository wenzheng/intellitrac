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

package pl.krakow.ae.knp.intellitrac.components;

import pl.krakow.ae.knp.intellitrac.gateway.TracGateway;
import pl.krakow.ae.knp.intellitrac.gateway.xmlrpc.XmlRpcTracGateway;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.intellij.openapi.ui.Messages;

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
        if (e.getSource() == testConnectionButton) {
          TracGateway gateway = new XmlRpcTracGateway();
          Messages.showMessageDialog(String.valueOf(gateway.isConnectionRight(tracUrl.getText(), login.getText(), password.getText())), "s", null);
        }
      }
    });
  }

  public JComponent getRootComponent() {
    return rootComponent;
  }

  public void setData(ConfigurationComponent data) {
    tracUrl.setText(data.getTracUrl());
    login.setText(data.getLogin());
    password.setText(data.getPassword());
  }

  public void getData(ConfigurationComponent data) {
    data.setTracUrl(tracUrl.getText());
    data.setLogin(login.getText());
    data.setPassword(password.getText());
  }

  public boolean isModified(ConfigurationComponent data) {
    if (tracUrl.getText() != null ? !tracUrl.getText().equals(data.getTracUrl()) : data.getTracUrl() != null)
      return true;
    if (login.getText() != null ? !login.getText().equals(data.getLogin()) : data.getLogin() != null) return true;
    if (password.getText() != null ? !password.getText().equals(data.getPassword()) : data.getPassword() != null)
      return true;
    return false;
  }

}