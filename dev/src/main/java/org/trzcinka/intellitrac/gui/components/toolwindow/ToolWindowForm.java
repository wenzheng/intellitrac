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
import org.trzcinka.intellitrac.gui.components.toolwindow.tickets.ReportEditorForm;
import org.trzcinka.intellitrac.gui.components.toolwindow.tickets.ReportsListForm;

import javax.swing.*;

/**
 * Acts as a controller. Reacts to state changes by adjusting GUI.
 */
public class ToolWindowForm implements StateListener {

  private JTabbedPane tabbedPane;
  private JPanel rootComponent;
  private JPanel ticketsContent;

  private DataPresenter reportsListForm;
  private DataPresenter reportEditorForm;
  private Project project;

  public ToolWindowForm(Project project) {
    this.project = project;
  }

  public JComponent getRootComponent() {
    return rootComponent;
  }

  private void createUIComponents() {
    ticketsContent = new JPanel();
    reportsListForm = new ReportsListForm(project, this);
    reportEditorForm = new ReportEditorForm(project, this);
    ticketsContent.add(reportsListForm.getRootComponent());

  }

  public void stateChanged(StateInfo stateInfo) {
    switch (stateInfo.getState()) {
      case REPORT_EDITOR:
        reportEditorForm.updateData(stateInfo.getInfo());
        ticketsContent.removeAll();
        ticketsContent.repaint();
        ticketsContent.add(reportEditorForm.getRootComponent());
        break;
      case REPORTS_LIST:
        ticketsContent.removeAll();
        ticketsContent.repaint();
        ticketsContent.add(reportsListForm.getRootComponent());
    }
  }
}