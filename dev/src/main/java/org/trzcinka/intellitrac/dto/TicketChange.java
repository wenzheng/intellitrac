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

import org.trzcinka.intellitrac.utils.Copiable;

import java.util.Date;

public class TicketChange implements Copiable<TicketChange> {

  public static final String COMMENT = "comment";

  private Date time;
  private String author;
  private String field;
  private String oldValue;
  private String newValue;
  private boolean permanent;

  public Date getTime() {
    return time;
  }

  public void setTime(Date time) {
    this.time = time;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  public String getOldValue() {
    return oldValue;
  }

  public void setOldValue(String oldValue) {
    this.oldValue = oldValue;
  }

  public String getNewValue() {
    return newValue;
  }

  public void setNewValue(String newValue) {
    this.newValue = newValue;
  }

  public boolean isPermanent() {
    return permanent;
  }

  public void setPermanent(boolean permanent) {
    this.permanent = permanent;
  }

  public TicketChange deepCopy() {
    TicketChange result = new TicketChange();
    result.author = author;
    result.field = field;
    result.newValue = newValue;
    result.oldValue = oldValue;
    result.permanent = permanent;
    result.time = time;
    return result;
  }

}