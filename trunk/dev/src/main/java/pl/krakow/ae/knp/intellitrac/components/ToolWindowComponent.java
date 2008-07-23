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

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.Content;
import com.intellij.peer.PeerFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NonNls;

import javax.swing.*;
import java.awt.*;

/**
 * 
 * Tool window component.
 *
 * @author Michal Trzcinka
 */
public class ToolWindowComponent implements ProjectComponent {
  private static final String COMPONENT_NAME = "IntelliTrac";
  private static final String TOOL_WINDOW_ID = "IntelliTracToolWindow";

  private Project project;
  private ToolWindow toolWindow;

  public ToolWindowComponent(Project project) {
    this.project = project;
  }

  public void initComponent() {
  }

  private void initToolWindow() {
  ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);

    JComponent panel = new JPanel(new BorderLayout());
    panel.add(new JLabel("Hello World!", JLabel.CENTER), BorderLayout.CENTER);

    toolWindow = toolWindowManager.registerToolWindow(TOOL_WINDOW_ID, false, ToolWindowAnchor.LEFT);
    ContentFactory contentFactory = PeerFactory.getInstance().getContentFactory();
    Content content = contentFactory.createContent(panel, COMPONENT_NAME, false);
    toolWindow.getContentManager().addContent(content);
  }

  public void disposeComponent() {
    unregisterToolWindow();
  }

  private void unregisterToolWindow() {
    ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
    toolWindowManager.unregisterToolWindow(TOOL_WINDOW_ID);
  }

  @NotNull
  public String getComponentName() {
    return COMPONENT_NAME;
  }

  public void projectOpened() {
    initToolWindow();
  }

  public void projectClosed() {
    // called when project is being closed
  }

}
