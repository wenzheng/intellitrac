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

package org.trzcinka.intellitrac.view.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.log4j.Logger;
import org.trzcinka.intellitrac.components.ConfigurationComponent;
import org.trzcinka.intellitrac.components.ToolWindowComponent;
import org.trzcinka.intellitrac.dto.Template;

/**
 * todo class description
 * <p/>
 * Created on: 2008-12-06 23:01:04 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Michal Trzcinka
 */

public abstract class BaseSendCodePointerAction extends AnAction {
  private static final Logger log = Logger.getLogger(BaseSendCodePointerAction.class.getName());

  public void actionPerformed(AnActionEvent event) {
    Project project = event.getData(DataKeys.PROJECT);
    Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
    VirtualFile file = event.getData(DataKeys.VIRTUAL_FILE);

    if (project != null && editor != null && file != null) {
      String selection;
      Template template;
      int line;

      if (editor.getSelectionModel().hasSelection()) {
        template = project.getComponent(ConfigurationComponent.class).getSendCodePointerSettings().getSelectionTemplate();
        selection = editor.getSelectionModel().getSelectedText();
        line = editor.getCaretModel().getLogicalPosition().line;
      } else {
        template = project.getComponent(ConfigurationComponent.class).getSendCodePointerSettings().getNoSelectionTemplate();
        selection = null;
        line = editor.getCaretModel().getLogicalPosition().line;
      }

      TemplateParams params = new TemplateParams(file.getName(), line, selection);
      ToolWindowComponent toolWindowComponent = project.getComponent(ToolWindowComponent.class);
      appendText(toolWindowComponent, TemplateProcessor.processTemplate(template, params));
    }
  }

  abstract void appendText(ToolWindowComponent toolWindowComponent, String text);

  

}