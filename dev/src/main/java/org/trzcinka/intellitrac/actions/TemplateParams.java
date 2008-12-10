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

/**
 * Holds ticket template params.
 *
 * @author Michal Trzcinka
 */

public class TemplateParams {

  public static final String FILE_NAME = "${filename}";
  public static final String LINE = "${line}";
  public static final String SELECTION = "${selection}";

  private String fileName;
  private int line;
  private String selection;

  public TemplateParams(String fileName, int line, String selection) {
    this.fileName = fileName;
    this.line = line;
    this.selection = selection;
  }

  public String getFileName() {
    return fileName;
  }

  public int getLine() {
    return line;
  }

  public String getSelection() {
    return selection;
  }
}