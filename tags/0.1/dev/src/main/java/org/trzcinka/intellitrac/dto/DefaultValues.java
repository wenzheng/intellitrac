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

import org.apache.log4j.Logger;

/**
 * <p/>
 * Created on: 2008-11-29 23:46:39 <br/>
 *
 * @author Michal Trzcinka
 */

public class DefaultValues {
  private static final Logger log = Logger.getLogger(DefaultValues.class.getName());

  private String component;
  private String priority;
  private String type;
  private String milestone;
  private String version;

  public String getComponent() {
    return component;
  }

  public void setComponent(String component) {
    this.component = component;
  }

  public String getPriority() {
    return priority;
  }

  public void setPriority(String priority) {
    this.priority = priority;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getMilestone() {
    return milestone;
  }

  public void setMilestone(String milestone) {
    this.milestone = milestone;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }
}