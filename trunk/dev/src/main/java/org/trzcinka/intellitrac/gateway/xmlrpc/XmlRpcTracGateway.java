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
import org.trzcinka.intellitrac.dto.TicketIdsList;
import org.trzcinka.intellitrac.dto.TracConfiguration;
import org.trzcinka.intellitrac.gateway.ConnectionFailedException;
import org.trzcinka.intellitrac.gateway.TracGateway;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class XmlRpcTracGateway implements TracGateway {
  private static final Object[] EMPTY_ARRAY = new Object[]{};

  private static TracGateway instance;

  private XmlRpcClient client;

  private XmlRpcTracGateway() {
  }

  public static TracGateway getInstance() {
    if (instance == null) {
      instance = new XmlRpcTracGateway();
    }
    return instance;
  }

  public void testConnection(TracConfiguration configuration) throws ConnectionFailedException {
    try {
      XmlRpcClient xmlRpcClient = prepareClient(configuration);
      xmlRpcClient.execute("system.listMethods", EMPTY_ARRAY);
    } catch (Exception e) {
      throw new ConnectionFailedException(e);
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

  /**
   * Sets the given configuration.
   *
   * @param configuration configuration.
   * @throws MalformedURLException when provided configuration URL is malformed.
   */
  public void setConfiguration(TracConfiguration configuration) throws MalformedURLException {
    client = prepareClient(configuration);
  }

  /**
   * {@inheritDoc}
   */
  public List<Ticket> retrieveTickets(String query) throws ConnectionFailedException {
    List<Ticket> result;
    try {
      Object response = client.execute("ticket.query", new Object[]{query});
      TicketIdsList list = new TicketIdsAdapter(response);
      result = new ArrayList<Ticket>(list.getTicketIdsList().length);
      for (Object ticketId : list.getTicketIdsList()) {
        result.add(retrieveTicket((Integer) ticketId));
      }
    } catch (XmlRpcException e) {
      throw new ConnectionFailedException(e);
    }
    return result;
  }


  private Ticket retrieveTicket(int ticketId) throws XmlRpcException {
    Object response = client.execute("ticket.get", new Object[]{ticketId});
    return new TicketAdapter(response);
  }

}