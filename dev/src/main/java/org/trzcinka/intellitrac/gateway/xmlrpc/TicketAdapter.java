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

package org.trzcinka.intellitrac.gateway.xmlrpc;

import com.intellij.openapi.diagnostic.Logger;
import org.apache.commons.lang.StringUtils;
import org.trzcinka.intellitrac.dto.Ticket;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

public class TicketAdapter extends Ticket {

  private static Logger logger = Logger.getInstance(TicketAdapter.class.getName());

  private static final int ORDER_ID = 0;

  /**
   * Instantiates new Ticket by adapting given object.
   *
   * @param adaptee not null.
   */
  public TicketAdapter(Object adaptee) {
    Object[] array = (Object[]) adaptee;
    setId((Integer) array[ORDER_ID]);
    setTimeCreated((Date) array[1]);
    setTimeChanged((Date) array[2]);
    Map<String, String> attributes = (Map<String, String>) array[3];
    for (String key : attributes.keySet()) {
      String value = attributes.get(key);
      setValue(key, value);
    }
  }

  /**
   * Uses reflection to set given property value.
   *
   * @param property property name.
   * @param value    value to set.
   */
  private void setValue(String property, String value) {
    try {
      Method setter = getClass().getMethod("set" + StringUtils.capitalize(property), String.class);
      setter.invoke(this, value);
    } catch (NoSuchMethodException e) {
      //nothing to do
    } catch (InvocationTargetException e) {
      logger.error("Could not instantiate Ticket.", e);
    } catch (IllegalAccessException e) {
      logger.error("Could not instantiate Ticket.", e);
    }
  }

}