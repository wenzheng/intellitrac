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

import org.trzcinka.intellitrac.dto.ConnectionSettings;
import org.trzcinka.intellitrac.dto.Ticket;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

/**
 * Represents communication with Trac server.
 */
public interface TracGateway {

  /**
   * Tests given configuration. Returns nothing if succeeded. Throws ConnectionFailedException
   * if there were problems during test.
   *
   * @param settings settings. Must be not null.
   * @throws ConnectionFailedException if there were problems during test.
   * @throws MalformedURLException     when the URL is malformed.
   */
  void testConnection(ConnectionSettings settings) throws ConnectionFailedException, MalformedURLException;

  /**
   * Sets the given configuration.
   *
   * @param settings settings. Must be not null.
   * @throws MalformedURLException when the provided URL is malformed.
   */
  void setConfiguration(ConnectionSettings settings) throws MalformedURLException;

  /**
   * @param query query. Must be not null.
   * @return not null list of tickets.
   * @throws ConnectionFailedException if there were connection problems.
   * @throws TracError                 if there were unknown Trac problems.
   */
  List<Ticket> retrieveTickets(String query) throws ConnectionFailedException, TracError;

  /**
   * @param ticketId ticket id.
   * @return ticket by id.
   * @throws ConnectionFailedException if there were connection problems.
   * @throws TracError                 if there were unknown Trac problems.
   */
  Ticket retrieveTicket(int ticketId) throws ConnectionFailedException, TracError;

  /**
   * @return list of components' names.
   * @throws ConnectionFailedException if there were connection problems.
   * @throws TracError                 if there were unknown Trac problems.
   */
  List<String> retrieveComponents() throws ConnectionFailedException, TracError;

  /**
   * @return list of priorities' names.
   * @throws ConnectionFailedException if there were connection problems.
   * @throws TracError                 if there were unknown Trac problems.
   */
  List<String> retrievePriorities() throws ConnectionFailedException, TracError;

  /**
   * @return list of types' names.
   * @throws ConnectionFailedException if there were connection problems.
   * @throws TracError                 if there were unknown Trac problems.
   */
  List<String> retrieveTypes() throws ConnectionFailedException, TracError;

  /**
   * @return list of milestones' names.
   * @throws ConnectionFailedException if there were connection problems.
   * @throws TracError                 if there were unknown Trac problems.
   */
  List<String> retrieveMilestones() throws ConnectionFailedException, TracError;

  /**
   * @return list of versions' names.
   * @throws ConnectionFailedException if there were connection problems.
   * @throws TracError                 if there were unknown Trac problems.
   */
  List<String> retrieveVersions() throws ConnectionFailedException, TracError;

  /**
   * @return list of resolutions' names.
   * @throws ConnectionFailedException if there were connection problems.
   * @throws TracError                 if there were unknown Trac problems.
   */
  List<String> retrieveResolutions() throws ConnectionFailedException, TracError;

  /**
   * Updates given ticket.
   *
   * @param ticket  not null ticket.
   * @param comment not null comment. If you do not want to add a comment, use
   *                {@link #saveTicket(Ticket)}.
   * @throws ConnectionFailedException if there were connection problems.
   * @throws TracError                 if there were unknown Trac problems.
   */
  void updateTicket(Ticket ticket, String comment) throws ConnectionFailedException, TracError;

  /**
   * Creates a new ticket.
   *
   * @param ticket not null ticket.
   * @return ticket id.
   * @throws ConnectionFailedException if there were connection problems.
   * @throws TracError                 if there were unknown Trac problems.
   */
  Integer saveTicket(Ticket ticket) throws ConnectionFailedException, TracError;

  /**
   * @param ticketId ticket id.
   * @param fileName attachment name.
   * @return attachment.
   * @throws ConnectionFailedException if there were connection problems.
   * @throws TracError                 if there were unknown Trac problems.
   */
  byte[] retrieveAttachment(int ticketId, String fileName) throws ConnectionFailedException, TracError;

  /**
   * Saves given attachment.
   *
   * @param ticketId    ticket id.
   * @param attachment  attachment file.
   * @param description description.
   * @param replace     if true and an attachment with given name already exists it will be overwritten.
   * @throws ConnectionFailedException if there were connection problems.
   * @throws TracError                 if there were unknown Trac problems.
   */
  void saveAttachment(int ticketId, File attachment, String description, boolean replace) throws ConnectionFailedException, TracError;

  /**
   * @param rawText text (may be null).
   * @return Renders given Wiki text in HTML.
   * @throws ConnectionFailedException if there were connection problems.
   * @throws TracError                 if there were unknown Trac problems.
   */
  String wikiToHtml(String rawText) throws ConnectionFailedException, TracError;
}