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

package org.trzcinka.intellitrac.view.toolwindow;

import org.trzcinka.intellitrac.view.BaseForm;
import org.trzcinka.intellitrac.view.toolwindow.tickets.TicketsTabForm;

import javax.swing.*;

public class ToolWindowForm extends BaseForm {

  private JTabbedPane tabbedPane;
  private JPanel rootComponent;

  private TicketsTabForm ticketsTabForm;

  public ToolWindowForm() {
  }

  private void createUIComponents() {
    tabbedPane = new JTabbedPane();
    ticketsTabForm = new TicketsTabForm();
    tabbedPane.addTab(bundle.getString("tool_window.tabs.tickets"), ticketsTabForm.getRootComponent());
  }

  public JComponent getRootComponent() {
    return rootComponent;
  }

  public void appendTextToComment(String text) {
    ticketsTabForm.appendTextToComment(text);
  }

  public void appendTextToDescription(String text) {
    ticketsTabForm.appendTextToDescription(text);
  }
}
