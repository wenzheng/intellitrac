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

package org.trzcinka.intellitrac.gateway;

import org.trzcinka.intellitrac.dto.TracConfiguration;
import org.trzcinka.intellitrac.dto.Ticket;

import java.net.MalformedURLException;
import java.util.List;

public interface TracGateway {

  /**
   * Tests given configuration. Returns nothing if succeeded. Throws ConnectionFailedException
   * if there were problems during test.
   *
   * @param configuration configuration. Must be not null.
   * @throws ConnectionFailedException if there were problems during test.
   */
  void testConnection(TracConfiguration configuration) throws ConnectionFailedException;

  /**
   * Sets the given configuration.
   *
   * @param configuration configuration. Must be not null.
   * @throws MalformedURLException when the provided URL is malformed.
   */
  void setConfiguration(TracConfiguration configuration) throws MalformedURLException;

  /**
   * @param query query. Must be not null.
   * @return not null list of tickets.
   * @throws IllegalStateException     when there is no configuration set.
   * @throws ConnectionFailedException if there were connection problems.
   */
  List<Ticket> retrieveTickets(String query) throws ConnectionFailedException;

}