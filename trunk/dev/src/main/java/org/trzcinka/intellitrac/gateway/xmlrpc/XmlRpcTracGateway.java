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

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.lang.StringUtils;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;
import org.trzcinka.intellitrac.dto.Ticket;
import org.trzcinka.intellitrac.dto.TicketChange;
import org.trzcinka.intellitrac.dto.TracConfiguration;
import org.trzcinka.intellitrac.gateway.ConnectionFailedException;
import org.trzcinka.intellitrac.gateway.TracError;
import org.trzcinka.intellitrac.gateway.TracGateway;

import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TracGateway implementation. Uses XML-RPC protocol to communicate with Trac.
 */
public class XmlRpcTracGateway implements TracGateway {
  private static final Object[] EMPTY_ARRAY = new Object[]{};

  private static TracGateway instance;

  private XmlRpcClient client;

  private boolean configurationSet;

  /**
   * Hidden constructor.
   */
  private XmlRpcTracGateway() {
  }

  public static TracGateway getInstance() {
    if (instance == null) {
      instance = new XmlRpcTracGateway();
    }
    return instance;
  }

  public void testConnection(TracConfiguration configuration) throws ConnectionFailedException, MalformedURLException {
    try {
      XmlRpcClient xmlRpcClient = prepareClient(configuration);
      xmlRpcClient.execute("system.listMethods", EMPTY_ARRAY);
    } catch (XmlRpcException e) {
      handleException(e);
    }
  }

  private XmlRpcClient prepareClient(TracConfiguration configuration) throws MalformedURLException {
    XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();

    config.setServerURL(new URL(StringUtils.removeEnd(configuration.getTracUrl(), "/") + "/login/xmlrpc"));
    XmlRpcClient xmlRpcClient = new XmlRpcClient();
    xmlRpcClient.setConfig(config);
    XmlRpcCommonsTransportFactory transportFactory = new XmlRpcCommonsTransportFactory(xmlRpcClient);
    Credentials credentials = new UsernamePasswordCredentials(configuration.getLogin(), configuration.getPassword());
    HttpClient httpClient = new HttpClient();
    httpClient.getState().setCredentials(AuthScope.ANY, credentials);
    transportFactory.setHttpClient(httpClient);
    xmlRpcClient.setTransportFactory(transportFactory);
    return xmlRpcClient;
  }

  private XmlRpcClient retrieveClient() throws ConnectionFailedException {
    if (!configurationSet) {
      throw new ConnectionFailedException("Could not connect to Trac server on startup. Verify your settings.");
    }
    assert client != null;
    return client;
  }

  /**
   * Sets the given configuration.
   *
   * @param configuration configuration.
   * @throws MalformedURLException when provided configuration URL is malformed.
   */
  public void setConfiguration(TracConfiguration configuration) throws MalformedURLException {
    client = prepareClient(configuration);
    configurationSet = true;
  }

  public List<Ticket> retrieveTickets(String query) throws ConnectionFailedException, TracError {
    List<Ticket> result = null;
    try {
      Object response = retrieveClient().execute("ticket.query", new Object[]{query});
      Integer[] ticketIdsList = CastUtils.castArray((Object[]) response, Integer.class);
      result = new ArrayList<Ticket>(ticketIdsList.length);
      for (Integer id : ticketIdsList) {
        result.add(retrieveTicket(id));
      }
    } catch (XmlRpcException e) {
      handleException(e);
    }
    return result;
  }

  private Ticket retrieveTicket(int ticketId) throws XmlRpcException, ConnectionFailedException {
    Object response = retrieveClient().execute("ticket.get", new Object[]{ticketId});
    Ticket ticket = new TicketAdapter(response);
    List<TicketChange> changes = retrieveTicketChanges(ticketId);
    ticket.setChanges(changes);
    return ticket;
  }

  private List<TicketChange> retrieveTicketChanges(int ticketId) throws ConnectionFailedException, XmlRpcException {
    Object[] responses = (Object[]) retrieveClient().execute("ticket.changeLog", new Object[]{ticketId});
    List<TicketChange> result = new ArrayList<TicketChange>(responses.length);
    for (Object response : responses) {
      TicketChangeAdapter change = new TicketChangeAdapter((Object[]) response);
      if (!(TicketChangeAdapter.COMMENT.equals(change.getField()) && StringUtils.isEmpty(change.getNewValue()))) {
        result.add(change);
      }
    }
    return result;
  }

  public List<String> retrieveComponents() throws ConnectionFailedException, TracError {
    return retrieveStringsList("ticket.component.getAll");
  }

  public List<String> retrievePriorities() throws ConnectionFailedException, TracError {
    return retrieveStringsList("ticket.priority.getAll");
  }

  public List<String> retrieveTypes() throws ConnectionFailedException, TracError {
    return retrieveStringsList("ticket.type.getAll");
  }

  public List<String> retrieveMilestones() throws ConnectionFailedException, TracError {
    return retrieveStringsList("ticket.milestone.getAll");
  }

  public List<String> retrieveVersions() throws ConnectionFailedException, TracError {
    return retrieveStringsList("ticket.version.getAll");
  }

  public Iterable<String> retrieveResolutions() throws ConnectionFailedException, TracError {
    return retrieveStringsList("ticket.resolution.getAll");
  }

  public void updateTicket(Ticket ticket, String comment) throws ConnectionFailedException, TracError {
    try {
      retrieveClient().execute("ticket.update", (Object[]) TicketAdapter.unadaptEditedTicket(ticket, comment));
    } catch (XmlRpcException e) {
      handleException(e);
    }
  }

  public void saveTicket(Ticket ticket) throws ConnectionFailedException, TracError {
    try {
      retrieveClient().execute("ticket.create", (Object[]) TicketAdapter.unadaptNewTicket(ticket));
    } catch (XmlRpcException e) {
      handleException(e);
    }
  }

  private List<String> retrieveStringsList(String function) throws ConnectionFailedException {
    List<String> result = null;
    try {
      Object response = retrieveClient().execute(function, EMPTY_ARRAY);
      String[] array = CastUtils.castArray((Object[]) response, String.class);
      result = Arrays.asList(array);
    } catch (XmlRpcException e) {
      handleException(e);
    }
    return result;
  }

  private void handleException(XmlRpcException e) throws ConnectionFailedException {
    if (e.getCause() != null && e.getCause() instanceof ConnectException) {
      throw new ConnectionFailedException(e);
    } else {
      throw new TracError(e);
    }
  }

}