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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Deep copy.
 * <p/>
 * Created on: 2008-12-06 19:33:47 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Michal Trzcinka
 */

public class DeepCopyUtils {
  private static final Logger log = Logger.getLogger(DeepCopyUtils.class.getName());

  public static <T extends Copiable<T>> List<T> deepCopy(Collection<T> list) {
    List<T> result = new ArrayList<T>(list.size());
    for (T t : list) {
      result.add(t.deepCopy());
    }
    return result;
  }

}