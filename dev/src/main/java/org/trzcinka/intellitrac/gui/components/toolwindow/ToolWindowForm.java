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
import org.trzcinka.intellitrac.gui.components.toolwindow.tickets.report_editor.ReportEditorForm;
import org.trzcinka.intellitrac.gui.components.toolwindow.tickets.reports_list.ReportsListForm;
import org.trzcinka.intellitrac.gui.components.toolwindow.tickets.tickets_list.TicketsListForm;

import javax.swing.*;
import java.awt.*;

/**
 * Acts as a controller. Reacts to state changes by adjusting GUI.
 */
public class ToolWindowForm implements StateListener {

  private static final String REPORTS_LIST = "REPORTS_LIST";
  private static final String REPORT_EDITOR = "REPORT_EDITOR";
  private static final String TICKETS_LIST = "TICKETS_LIST";

  private JTabbedPane tabbedPane;
  private JPanel rootComponent;
  private JPanel ticketsContent;

  private DataPresenter reportsListForm;
  private DataPresenter reportEditorForm;
  private DataPresenter ticketsListForm;

  private Project project;
  private CardLayout cardLayout;

  private StateInfo lastState;

  public ToolWindowForm(Project project) {
    this.project = project;
  }

  public JComponent getRootComponent() {
    return rootComponent;
  }

  private void createUIComponents() {
    cardLayout = new CardLayout();
    ticketsContent = new JPanel(cardLayout);

    reportsListForm = new ReportsListForm(project, this);
    reportEditorForm = new ReportEditorForm(project, this);
    ticketsListForm = new TicketsListForm(project, this);

    ticketsContent.add(REPORTS_LIST, reportsListForm.getRootComponent());
    ticketsContent.add(REPORT_EDITOR, reportEditorForm.getRootComponent());
    ticketsContent.add(TICKETS_LIST, ticketsListForm.getRootComponent());
  }

  public void stateChanged(StateInfo stateInfo) {
    
    switch (stateInfo.getState()) {
      case REPORT_EDITOR:
        reportEditorForm.updateData(stateInfo.getInfo());
        cardLayout.show(ticketsContent, REPORT_EDITOR);
        break;
      case REPORTS_LIST:
        reportsListForm.updateData(stateInfo.getInfo());
        cardLayout.show(ticketsContent, REPORTS_LIST);
        break;
      case TICKETS_LIST:
        ticketsListForm.updateData(stateInfo.getInfo());
        cardLayout.show(ticketsContent, TICKETS_LIST);
        break;
      case BACK:              //TODO: last state
        stateChanged(lastState);
        break;
    }
  }
}