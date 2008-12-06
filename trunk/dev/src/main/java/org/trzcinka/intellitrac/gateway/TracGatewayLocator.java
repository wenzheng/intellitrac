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

import com.intellij.openapi.ui.Messages;
import org.trzcinka.intellitrac.BundleLocator;
import org.trzcinka.intellitrac.gateway.xmlrpc.XmlRpcTracGateway;

import java.util.ResourceBundle;

/**
 * Should be used to retrieve TracGateway instance.
 */
public class TracGatewayLocator {

  private static ResourceBundle bundle = BundleLocator.getBundle();

  public static TracGateway retrieveTracGateway() {
    return XmlRpcTracGateway.getInstance();
  }

  public static void handleConnectionProblem() {
    Messages.showMessageDialog(bundle.getString("configuration.connection.dialogs.connection_failed"), bundle.getString("dialogs.error"), Messages.getErrorIcon());
  }

}