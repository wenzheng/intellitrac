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

package org.trzcinka.intellitrac.gui.components;

import org.testng.annotations.Test;
import org.trzcinka.intellitrac.dto.Report;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"MagicNumber"})
@Test
public class ReportsConfigurationComponentTest {


  public void testUpdateReport() {
    ReportsConfigurationComponent component = new ReportsConfigurationComponent();
    component.setReports(generateReports());
    Report updatedReport2 = new Report();
    final String updatedReportText = "Updated report2";
    updatedReport2.setDescription(updatedReportText);
    updatedReport2.setId(2L);
    updatedReport2.setName(updatedReportText);
    updatedReport2.setQuery(updatedReportText);
    component.updateReport(updatedReport2);

    assert component.getReports().get(1).getDescription().equals(updatedReportText);
    assert component.getReports().get(1).getId().equals(2L);
    assert component.getReports().get(1).getName().equals(updatedReportText);
    assert component.getReports().get(1).getQuery().equals(updatedReportText);
  }

  private List<Report> generateReports() {
    List<Report> result = new ArrayList<Report>(3);
    Report report1 = new Report();
    report1.setDescription("Report1");
    report1.setId(1L);
    report1.setName("Report1");
    report1.setQuery("Report1");

    Report report2 = new Report();
    report2.setDescription("Report2");
    report2.setId(2L);
    report2.setName("Report2");
    report2.setQuery("Report2");

    Report report3 = new Report();
    report3.setDescription("Report3");
    report3.setId(3L);
    report3.setName("Report3");
    report3.setQuery("Report3");

    result.add(report1);
    result.add(report2);
    result.add(report3);

    return result;
  }

}