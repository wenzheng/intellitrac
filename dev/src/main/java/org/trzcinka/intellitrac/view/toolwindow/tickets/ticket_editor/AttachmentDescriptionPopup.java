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

package org.trzcinka.intellitrac.view.toolwindow.tickets.ticket_editor;

import org.apache.log4j.Logger;

import javax.swing.*;

/**
 * <p/>
 * Created on: 2008-11-02 23:26:12 <br/>
 *
 * @author Michal Trzcinka
 */

public class AttachmentDescriptionPopup extends JOptionPane {
  private static final Logger log = Logger.getLogger(AttachmentDescriptionPopup.class.getName());
  private static final int MAX_CHARS_PER_LINE = 60;

  public AttachmentDescriptionPopup(Object message) {
    super(message);
  }

  @Override
  public int getMaxCharactersPerLineCount() {
    return MAX_CHARS_PER_LINE;
  }

}