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

package org.trzcinka.intellitrac.gui.utils;

import javax.swing.*;

/**
 * Holds icons used by the plugin.
 */
public class IntelliTracIcons {

  private static IntelliTracIcons instance;

  private Icon bigIcon;
  private Icon smallIcon;

  private IntelliTracIcons() {
    bigIcon = new ImageIcon(getClass().getClassLoader().getResource("images/big_logo.png"));
    smallIcon = new ImageIcon(getClass().getClassLoader().getResource("images/small_logo.png"));
  }

  public static IntelliTracIcons getInstance() {
    if (instance == null) {
      instance = new IntelliTracIcons();
    }
    return instance;
  }

  public Icon getBigIcon() {
    return bigIcon;
  }

  public Icon getSmallIcon() {
    return smallIcon;
  }
}