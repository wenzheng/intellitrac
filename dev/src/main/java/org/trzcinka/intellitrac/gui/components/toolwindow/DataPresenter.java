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

package org.trzcinka.intellitrac.gui.components.toolwindow;

import javax.swing.*;

/**
 * Classes implementing this interface will be usually forms which should be able
 * to update its content.
 */
public interface DataPresenter {

  /**
   * This method is invoked by controller when it receives a change of state notification.
   * Classes implementing this interface should update its data based on given state change info.
   *
   * @param info info.
   */
  void updateData(Object info);

  /**
   * @return root component.
   */
  JComponent getRootComponent();

}