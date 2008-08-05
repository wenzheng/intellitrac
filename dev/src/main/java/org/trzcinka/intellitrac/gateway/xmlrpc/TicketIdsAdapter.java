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

import org.trzcinka.intellitrac.dto.TicketIdsList;

/**
 * Adapts data received from XML-RPC service to ticket ids list.
 */
public class TicketIdsAdapter extends TicketIdsList {

  public TicketIdsAdapter(Object adaptee) {
    Object[] array = (Object[]) adaptee;
    int[] result = new int[array.length];
    for (int i = 0; i < array.length; i++) {
      result[i] = (Integer) array[i];
    }
    setTicketIdsList(result);
  }

}