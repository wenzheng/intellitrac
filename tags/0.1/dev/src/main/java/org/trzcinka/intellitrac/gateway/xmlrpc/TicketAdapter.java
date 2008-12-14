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
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.trzcinka.intellitrac.dto.Ticket;
import org.trzcinka.intellitrac.gateway.TracError;
import org.trzcinka.intellitrac.utils.StringConversionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Adapts data received from XML-RPC service to the Ticket class.
 */
public class TicketAdapter extends Ticket {

  private static Logger logger = Logger.getInstance(TicketAdapter.class.getName());

  /**
   * Instantiates new Ticket by adapting given object.
   *
   * @param adaptee not null.
   */
  public TicketAdapter(Object adaptee) {
    Object[] array = (Object[]) adaptee;
    setId((Integer) array[0]);
    setTimeCreated((Date) array[1]);
    setTimeChanged((Date) array[2]);
    Map<String, String> attributes = (Map<String, String>) array[3];
    try {
      BeanUtils.populate(this, attributes);
    } catch (Exception e) {
      logger.error("Could not instantiate TicketAdapter", e);
      throw new TracError(e);
    }
  }

  /**
   * Convets given ticket into a format proper for XML-RPC Trac communication.
   *
   * @param ticket ticket.
   * @return .
   */
  public static Object unadaptNewTicket(Ticket ticket) {
    Object[] result = new Object[3];
    result[0] = ticket.getSummary();
    result[1] = ticket.getDescription();
    try {
      result[2] = describe(ticket);
    } catch (Exception e) {
      logger.error("Could not convert Ticket instance to a format apprioriate for Trac communication", e);
      throw new TracError(e);
    }

    return result;
  }

  /**
   * Convets given ticket into a format proper for XML-RPC Trac communication.
   *
   * @param ticket ticket.
   * @return .
   */
  public static Object unadaptEditedTicket(Ticket ticket, String comment) {
    Object[] result = new Object[3];
    result[0] = ticket.getId();
    result[1] = comment;
    try {
      result[2] = describe(ticket);
    } catch (Exception e) {
      logger.error("Could not convert Ticket instance to a format apprioriate for Trac communication", e);
      throw new TracError(e);
    }

    return result;
  }

  /**
   * Transforms given ticket into a properties map. Properties with null values aren't put into the map.
   *
   * @param ticket ticket.
   * @return map.
   * @throws InvocationTargetException
   * @throws NoSuchMethodException
   * @throws IllegalAccessException
   */
  private static Map<String, Object> describe(Ticket ticket) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
    final String[] SERIALIZED_PROPERTIES = {"summary", "keywords", "status", "resolution", "type", "version", "reporter", "milestone", "component", "description", "priority", "owner", "cc"};
    Map<String, Object> result = new HashMap<String, Object>(SERIALIZED_PROPERTIES.length);
    for (String PROPERTY : SERIALIZED_PROPERTIES) {
      String key = StringConversionUtils.toUnderscore(PROPERTY);
      Object value = PropertyUtils.getProperty(ticket, PROPERTY);
      if (value != null) {
        result.put(key, value);
      }
    }
    return result;
  }


}