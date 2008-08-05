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

package org.trzcinka.intellitrac.gui.components.toolwindow.tickets;

import com.intellij.openapi.project.Project;
import org.trzcinka.intellitrac.gui.components.toolwindow.State;
import org.trzcinka.intellitrac.gui.components.toolwindow.StateInfo;
import org.trzcinka.intellitrac.gui.components.toolwindow.StateListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConstantToolbarForm {

  private JPanel rootComponent;
  private JToolBar toolbar;
  private JButton newTicket;
  private JButton goHome;
  private JButton goBack;

  private Project project;
  private StateListener stateListener;

  public ConstantToolbarForm(Project project, final StateListener stateListener) {
    this.project = project;
    this.stateListener = stateListener;
    goHome.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        StateInfo stateInfo = new StateInfo(State.REPORTS_LIST, null);
        stateListener.stateChanged(stateInfo);
      }
    });
    goBack.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        stateListener.goBack();
      }
    });
    newTicket.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        StateInfo stateInfo = new StateInfo(State.TICKET_EDITOR, null);
        stateListener.stateChanged(stateInfo);
      }
    });
  }
}