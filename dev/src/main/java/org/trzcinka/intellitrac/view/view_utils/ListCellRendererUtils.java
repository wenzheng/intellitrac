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

import javax.swing.*;

/**
 * todo class description
 * <p/>
 * Created on: 2008-11-09 19:20:16 <br/>
 *
 * @author Michal Trzcinka
 */

public final class ListCellRendererUtils {
  private static final Logger log = Logger.getLogger(ListCellRendererUtils.class.getName());

  private static final ListCellRenderer defaultRenderer = new DefaultListCellRenderer();

  private ListCellRendererUtils() {
  }

  /**
   * Uses {@link DefaultListCellRenderer} to apply default settings to the provided renderer component.
   *
   * @param list         list.
   * @param index        index.
   * @param selected     selected.
   * @param cellHasFocus focus.
   * @param renderer     renderer component.
   */
  public static void applyDefaultDisplaySettings(JList list, int index, boolean selected, boolean cellHasFocus, JComponent renderer) {

    JComponent defRendererComponent = (JComponent) defaultRenderer.getListCellRendererComponent(list, cellHasFocus, index, selected, cellHasFocus);
    renderer.setForeground(defRendererComponent.getForeground());
    renderer.setBackground(defRendererComponent.getBackground());
    renderer.setComponentOrientation(defRendererComponent.getComponentOrientation());
    if (cellHasFocus) {
      renderer.requestFocus();
    }
    renderer.setBorder(defRendererComponent.getBorder());
    renderer.setEnabled(defRendererComponent.isEnabled());
    renderer.setFont(defRendererComponent.getFont());
  }

}