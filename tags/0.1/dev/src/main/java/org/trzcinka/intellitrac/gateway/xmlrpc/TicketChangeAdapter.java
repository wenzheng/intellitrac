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

import org.trzcinka.intellitrac.dto.TicketChange;

import java.util.Date;

public class TicketChangeAdapter extends TicketChange {

  public TicketChangeAdapter(Object[] adaptee) {
    setTime((Date) adaptee[0]);
    setAuthor((String) adaptee[1]);
    setField((String) adaptee[2]);
    setOldValue((String) adaptee[3]);
    setNewValue((String) adaptee[4]);
    setPermanent((Integer) adaptee[5] == 1);
  }
}