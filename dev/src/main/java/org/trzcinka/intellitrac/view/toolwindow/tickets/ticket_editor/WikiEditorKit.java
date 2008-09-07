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

import javax.swing.*;
import javax.swing.text.*;
import java.io.*;

public class WikiEditorKit extends EditorKit {

  public String getContentType() {
    return null;  //TODO: Implement
  }

  public ViewFactory getViewFactory() {
    return null;  //TODO: Implement
  }

  public Action[] getActions() {
    return new Action[0];  //TODO: Implement
  }

  public Caret createCaret() {
    return null;  //TODO: Implement
  }

  public Document createDefaultDocument() {
    return null;  //TODO: Implement
  }

  public void read(InputStream in, Document doc, int pos) throws IOException, BadLocationException {
//TODO: Implement
  }

  public void write(OutputStream out, Document doc, int pos, int len) throws IOException, BadLocationException {
//TODO: Implement
  }

  public void read(Reader in, Document doc, int pos) throws IOException, BadLocationException {
//TODO: Implement
  }

  public void write(Writer out, Document doc, int pos, int len) throws IOException, BadLocationException {
//TODO: Implement
  }
}