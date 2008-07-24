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
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;
import org.trzcinka.intellitrac.gateway.ConnectionFailedException;
import org.trzcinka.intellitrac.gateway.TracGateway;
import org.trzcinka.intellitrac.dto.TracConfigurationBean;

import java.net.URL;

public class XmlRpcTracGateway implements TracGateway {
  private static final Object[] EMPTY_ARRAY = new Object[]{};

  private static TracGateway instance;

  private TracConfigurationBean.TracConfiguration configuration;

  private XmlRpcTracGateway() {
  }

  public static TracGateway getInstance() {
    if (instance == null) {
      instance = new XmlRpcTracGateway();
    }
    return instance;
  }

  public void testConnection(TracConfigurationBean.TracConfiguration configuration) throws ConnectionFailedException {
    try {
      XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();

      config.setServerURL(new URL(configuration.getTracUrl()));
      XmlRpcClient xmlRpcClient = new XmlRpcClient();
      xmlRpcClient.setConfig(config);
      XmlRpcCommonsTransportFactory transportFactory = new XmlRpcCommonsTransportFactory(xmlRpcClient);
      Credentials credentials = new UsernamePasswordCredentials(configuration.getLogin(), configuration.getPassword());
      HttpClient httpClient = new HttpClient();
      httpClient.getState().setCredentials(AuthScope.ANY, credentials);
      transportFactory.setHttpClient(httpClient);
      xmlRpcClient.setTransportFactory(transportFactory);

      xmlRpcClient.execute("system.listMethods", EMPTY_ARRAY);

    } catch (Exception e) {
      throw new ConnectionFailedException(e);
    }
  }

  /**
   * Sets the given configuration.
   *
   * @param configuration configuration.
   */
  public void setConfiguration(TracConfigurationBean.TracConfiguration configuration) {
    this.configuration = configuration;
  }

}