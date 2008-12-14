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

package org.trzcinka.intellitrac.utils;

import org.apache.log4j.Logger;

import java.util.List;

/**
 * Collections-related utility methods.
 * <p/>
 * Created on: 2008-12-06 19:53:45 <br/>
 *
 * @author Michal Trzcinka
 */

public final class CollectionsUtils {
  private static final Logger log = Logger.getLogger(CollectionsUtils.class.getName());

  /**
   * Checks if given lists contain <i>equal</i> elements <b>in the same order</b>.
   * An element <tt>x</tt> is considered <i>equal</i> to element <tt>y</tt>, if <tt>x.equals(y)</tt> returns <tt>true</tt>.
   *
   * @param a not null first collection.
   * @param b not null second collection.
   */
  public static boolean areListsEqual(List a, List b) {
    if (a == b) {
      return true;
    }
    if (a.size() != b.size()) {
      return false;
    }
    for (int i = 0; i < a.size(); i++) {
      if (!(a.get(i).equals(b.get(i)))) {
        return false;
      }
    }
    return true;
  }

}