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

package org.trzcinka.intellitrac.view.view_utils;

import org.apache.log4j.Logger;
import org.trzcinka.intellitrac.gateway.ConnectionFailedException;

import javax.swing.*;

/**
 * todo class description
 * <p/>
 * Created on: 2008-11-30 00:10:59 <br/>
 *
 * @author Michal Trzcinka
 */

public final class FormUtils {
  private static final Logger log = Logger.getLogger(FormUtils.class.getName());

  /**
   * Fills given comboBoxModel with provided elements.
   *
   * @param comboBoxModel     not null comboBoxModel.
   * @param elements          not null elements.
   * @param selected          element to select.
   * @param selectFirstIfNull if true and selected is null, first element will be selected. If false and selected is null, no elements will be selected.
   * @throws org.trzcinka.intellitrac.gateway.ConnectionFailedException
   *          in case of connection problems.
   */
  public static void fillComboBox(DefaultComboBoxModel comboBoxModel, Iterable<String> elements, Object selected, boolean selectFirstIfNull) throws ConnectionFailedException {
    comboBoxModel.removeAllElements();
    boolean empty = true;
    for (String component : elements) {
      comboBoxModel.addElement(component);
      empty = false;
    }
    if (selected != null) {
      comboBoxModel.setSelectedItem(selected);
    } else { // selected is null
      if (selectFirstIfNull && !empty) {
        comboBoxModel.setSelectedItem(comboBoxModel.getElementAt(0));
      } else if (!selectFirstIfNull) {
        comboBoxModel.setSelectedItem(null);
      }
    }

  }

}