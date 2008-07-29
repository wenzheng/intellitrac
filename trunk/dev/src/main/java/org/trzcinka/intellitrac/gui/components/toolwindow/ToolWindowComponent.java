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

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.peer.PeerFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;
import org.trzcinka.intellitrac.BundleLocator;
import org.trzcinka.intellitrac.gui.utils.IntelliTracIcons;
import org.trzcinka.intellitrac.gui.components.toolwindow.ToolWindowForm;

/**
 * Tool window component.
 *
 * @author Michal Trzcinka
 */
public class ToolWindowComponent implements ProjectComponent {
  private static final String COMPONENT_NAME = BundleLocator.getBundle().getString("tool_window.title");

  private static final String TOOL_WINDOW_ID = "IntelliTrac";

  private Project project;
  private ToolWindow toolWindow;

  public ToolWindowComponent(Project project) {
    this.project = project;
  }

  public void initComponent() {
  }

  private void initToolWindow() {
    ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);

    toolWindow = toolWindowManager.registerToolWindow(TOOL_WINDOW_ID, false, ToolWindowAnchor.RIGHT);
    toolWindow.setIcon( IntelliTracIcons.getInstance().getSmallIcon() );

    ContentFactory contentFactory = PeerFactory.getInstance().getContentFactory();
    Content content = contentFactory.createContent(new ToolWindowForm(project).getRootComponent(), null, false);

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
