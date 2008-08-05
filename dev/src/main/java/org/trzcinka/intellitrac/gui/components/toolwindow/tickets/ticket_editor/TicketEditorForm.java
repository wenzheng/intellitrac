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

package org.trzcinka.intellitrac.gui.components.toolwindow.tickets.ticket_editor;

import com.intellij.openapi.project.Project;
import org.trzcinka.intellitrac.gui.components.toolwindow.DataPresenter;
import org.trzcinka.intellitrac.gui.components.toolwindow.StateListener;
import org.trzcinka.intellitrac.gui.components.toolwindow.tickets.ConstantToolbarForm;

import javax.swing.*;

public class TicketEditorForm implements DataPresenter {
  private Project project;
  private StateListener stateListener;
  private JPanel rootComponent;
  private ConstantToolbarForm constantToolbarForm;

  public TicketEditorForm(Project project, StateListener stateListener) {
    this.project = project;
    this.stateListener = stateListener;
  }

  public void updateData(Object info) {
    //TODO: Implement
  }

  public JComponent getRootComponent() {
    return rootComponent;
  }

  private void createUIComponents() {
    constantToolbarForm = new ConstantToolbarForm(project, stateListener);
  }
}