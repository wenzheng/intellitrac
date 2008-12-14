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

import org.testng.annotations.Test;

import java.util.Arrays;

@SuppressWarnings({"MagicNumber"})
@Test
public class CastArrayTest {

  public void testSimpleCast() {
    Object[] array = new Object[]{1, 2, 3, 4};
    Integer[] result = CastUtils.castArray(array, Integer.class);
    assert Arrays.equals(array, result);
  }

  @Test(expectedExceptions = ArrayStoreException.class)
  public void testArrayStoreException() {
    Object[] array = new Object[]{1, 2, "foo", 5L};
    CastUtils.castArray(array, Integer.class);
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void testNullPointerException() {
    CastUtils.castArray(null, null);
  }

}