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

import org.apache.log4j.Logger;
import org.trzcinka.intellitrac.dto.Attachment;

import java.util.Date;

/**
 * Adapts XML-RPC response to Attachment.
 * <p/>
 * Created on: 2008-09-21 00:05:14 <br/>
 *
 * @author Michal Trzcinka
 */

public class AttachmentAdapter extends Attachment {
  private static final Logger log = Logger.getLogger(AttachmentAdapter.class.getName());

  public AttachmentAdapter(Object adaptee) {
    Object[] array = (Object[]) adaptee;
    setFileName((String) array[0]);
    setDescription((String) array[1]);
    setSize((Integer) array[2]);
    setTime((Date) array[3]);
    setAuthor((String) array[4]);
  }

}