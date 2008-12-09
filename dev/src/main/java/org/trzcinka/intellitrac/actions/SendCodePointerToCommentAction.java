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

package org.trzcinka.intellitrac.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import org.apache.log4j.Logger;
import org.trzcinka.intellitrac.components.ToolWindowComponent;
import org.trzcinka.intellitrac.model.tickets.State;
import org.trzcinka.intellitrac.model.tickets.TicketsModel;

/**
 * todo class description
 * <p/>
 * Created on: 2008-11-29 22:15:21 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Michal Trzcinka
 */

public class SendCodePointerToCommentAction extends BaseSendCodePointerAction {
  private static final Logger log = Logger.getLogger(SendCodePointerToCommentAction.class.getName());

  void appendText(ToolWindowComponent toolWindowComponent, String text) {
    toolWindowComponent.appendTextToComment(text);
  }

  @Override
  public void update(AnActionEvent e) {
    State state = TicketsModel.getInstance().getCurrentState();
    boolean enabled = state == State.TICKET_EDITOR;
    e.getPresentation().setEnabled(enabled);
  }
}