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

package org.trzcinka.intellitrac.model.tickets.reports_list;

import org.trzcinka.intellitrac.dto.Report;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents report list model. GUI objects should refer to this class to get/update data
 * concerning report list.
 */
public class ReportsListModel extends AbstractListModel implements ReportsListListener {

  private List<Report> reports;

  public ReportsListModel() {
    this.reports = new ArrayList<Report>();
  }

  /**
   * {@inheritDoc}
   */
  public int getSize() {
    return reports != null ? reports.size() : 0;
  }

  /**
   * {@inheritDoc}
   */
  public Object getElementAt(int index) {
    return reports.get(index);
  }

  /**
   * Saves a report - adds a new one if its ID is null, otherwise performs update.
   *
   * @param report must be not null.
   */
  public void saveReport(Report report) {
    if (report.getId() != null) {
      updateReport(report);
    } else {
      doSaveReport(report);
    }
  }

  private void doSaveReport(Report report) {
    long id = retrieveNextReportId();
    report.setId(id);
    reports.add(report);
    int index = reports.indexOf(report);
    fireIntervalAdded(this, index, index);
  }

  private long retrieveNextReportId() {
    long maxId = 1;
    for (Report report : reports) {
      if (report.getId() > maxId) {
        maxId = report.getId();
      }
    }
    return maxId + 1;
  }

  private void updateReport(Report report) {
    for (int i = 0; i < reports.size(); i++) {
      Report r = reports.get(i);
      if (r.getId().equals(report.getId())) {
        reports.set(i, report);
        fireContentsChanged(this, i, i);
      }
    }
  }

  /**
   * Removes given report.
   *
   * @param report must be not null.
   */
  public void removeReport(Report report) {
    int index = -1;
    for (int i = 0; i < reports.size(); i++) {
      Report r = reports.get(i);
      if (r == report) {
        index = i;
      }
    }
    if (index != -1) {
      reports.remove(index);
      fireIntervalRemoved(this, index, index);
    }
  }

  public void reportsListChanged(List<Report> reportList) {
    this.reports = reportList;
    fireContentsChanged(this, 0, reportList.size() - 1);
  }

  public void setReportListHolder(ReportsListHolder reportsListHolder) {
    reportsListHolder.addReportsListListener(this);
    reports = reportsListHolder.getReports();
  }

}