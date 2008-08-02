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

package org.trzcinka.intellitrac.dto;

/**
 * Represents a tickets report.
 */
public class Report {

  private Long id;
  private String name;
  private String description;
  private String query;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;

    Report report = (Report) obj;

    if (!description.equals(report.description)) return false;
    if (!id.equals(report.id)) return false;
    if (!name.equals(report.name)) return false;
    if (!query.equals(report.query)) return false;

    return true;
  }

  public int hashCode() {
    int result;
    result = id.hashCode();
    result = 31 * result + name.hashCode();
    result = 31 * result + description.hashCode();
    result = 31 * result + query.hashCode();
    return result;
  }
}