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
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.WindowManager;
import org.apache.log4j.Logger;
import org.trzcinka.intellitrac.model.ApplicationModel;

import javax.swing.*;
import java.awt.*;

/**
 * todo class description
 * <p/>
 * Created on: 2008-11-29 22:15:21 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Michal Trzcinka
 */

public class SendCodePointerToCommentAction extends AnAction {
  private static final Logger log = Logger.getLogger(SendCodePointerToCommentAction.class.getName());

  public void actionPerformed(AnActionEvent event) {
    Project project = ApplicationModel.getInstance().getProject();
    WindowManager.getInstance().getStatusBar(project).fireNotificationPopup(new JLabel("aaa"), Color.BLUE);
    Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();

    if (editor.getSelectionModel().hasSelection()) {
      popup(editor.getSelectionModel().getSelectedText());
    } else {
      int line = editor.getCaretModel().getLogicalPosition().line;
      int column = editor.getCaretModel().getLogicalPosition().column;
      popup(line + ":" + column);
    }

  }

  private void popup(String text) {
    Messages.showMessageDialog(text, "", null);
  }

}