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

import java.lang.reflect.Array;


public final class CastUtils {

  /**
   * Casts given array of Objects to array of desired classes' objects. Example:
   * <p><blockquote><pre>
   * Object[] array = new Object[] {1, 2, 3};
   * Integer[] ints = CastUtils.castArray(array, Integer.class);
   * </pre></blockquote></p>
   *
   * @param array not null input array.
   * @param clazz not null desired class.
   * @return array.
   * @throws ArrayStoreException when given array contains at least one element which is not of clazz class.
   */
  @SuppressWarnings({"unchecked"})
  public static <T> T[] castArray(Object[] array, Class<T> clazz) throws ArrayStoreException {
    T[] result = (T[]) Array.newInstance(clazz, array.length);
    for (int i = 0; i < array.length; i++) {
      T element = (T) array[i];
      result[i] = element;
    }
    return result;
  }


}