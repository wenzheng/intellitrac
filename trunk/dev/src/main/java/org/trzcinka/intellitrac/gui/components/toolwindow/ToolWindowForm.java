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

package org.trzcinka.intellitrac.gui.components.toolwindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.ide.DataManager;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import org.trzcinka.intellitrac.gui.components.ReportsConfigurationComponent;
import org.trzcinka.intellitrac.gui.components.toolwindow.tickets.TicketsForm;

public class ToolWindowForm {

  private JTabbedPane tabbedPane;
  private JPanel rootComponent;
  private TicketsForm ticketsForm;

  private Project project;

  public ToolWindowForm(Project project) {
    this.project = project;
  }

  public JComponent getRootComponent() {
    return rootComponent;
  }

  private void createUIComponents() {
    ticketsForm = new TicketsForm(project); 
  }

}