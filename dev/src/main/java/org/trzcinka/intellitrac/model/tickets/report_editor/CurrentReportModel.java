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

package org.trzcinka.intellitrac.model.tickets.report_editor;

import org.trzcinka.intellitrac.dto.Report;
import org.trzcinka.intellitrac.model.tickets.CurrentReportListener;

import java.util.ArrayList;
import java.util.Collection;

public class CurrentReportModel {

  private Report currentReport;

  private Collection<CurrentReportListener> listeners;

  public CurrentReportModel() {
    listeners = new ArrayList<CurrentReportListener>();
  }

  /**
   * Adds given object to the list of listeners. Listeners will be notified about state changes.
   *
   * @param listener not null listener.
   */
  public void addListener(CurrentReportListener listener) {
    listeners.add(listener);
  }

  private void notifyListeners() {
    for (CurrentReportListener listener : listeners) {
      listener.currentReportChanged(currentReport);
    }
  }

  public Report getCurrentReport() {
    return currentReport;
  }

  public void setCurrentReport(Report currentReport) {
    this.currentReport = currentReport;
    notifyListeners();
  }

}