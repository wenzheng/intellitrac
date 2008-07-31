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
import org.trzcinka.intellitrac.dto.Report;
import org.trzcinka.intellitrac.gui.components.toolwindow.StateListener;

import javax.swing.*;
import java.awt.*;

public class ReportEditorForm {

  private JTextField name;
  private JTextField description;
  private JTextField query;
  private JPanel rootComponent;

  private Project project;
  private StateListener stateListener;

  public ReportEditorForm(Project project, StateListener stateListener) {
    this.project = project;
    this.stateListener = stateListener;
  }

  public void setData(Report data) {
    name.setText(data.getName());
    description.setText(data.getDescription());
    query.setText(data.getQuery());
  }

  public void getData(Report data) {
    data.setName(name.getText());
    data.setDescription(description.getText());
    data.setQuery(query.getText());
  }

  public Component getRootComponent() {
    return rootComponent;
  }

}