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

package org.trzcinka.intellitrac;

import org.testng.annotations.Test;
import org.trzcinka.intellitrac.view.view_utils.IntelliTracIcons;
import org.trzcinka.intellitrac.view.view_utils.MouseCursors;

@Test
public class ViewUtilsTest {

  public void testIcons() {
    IntelliTracIcons icons = IntelliTracIcons.getInstance();
    assert icons != null;

    assert icons.getBigIcon() != null;
    assert icons.getBigIcon().getIconHeight() > 0;
    assert icons.getBigIcon().getIconWidth() > 0;

    assert icons.getSmallIcon() != null;
    assert icons.getSmallIcon().getIconHeight() > 0;
    assert icons.getSmallIcon().getIconWidth() > 0;
  }

  public void testCursors() {
    assert MouseCursors.WAIT_CURSOR != null;
    assert MouseCursors.WAIT_CURSOR.getName() != null;
  }

}