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

package org.trzcinka.intellitrac.components;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.ho.yaml.Yaml;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.trzcinka.intellitrac.dto.*;
import org.trzcinka.intellitrac.gateway.TracGatewayLocator;
import org.trzcinka.intellitrac.utils.DeepCopyUtils;
import org.trzcinka.intellitrac.view.configuration.ConfigurationForm;
import org.trzcinka.intellitrac.view.view_utils.IntelliTracIcons;

import javax.swing.*;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents per-project plugin configuration.
 */
@State(name = ConfigurationComponent.COMPONENT_NAME,
  storages = @Storage(id = "GeneralSettings", file = "$PROJECT_FILE$"))
public class ConfigurationComponent implements ProjectComponent, Configurable,
  PersistentStateComponent<ConfigurationComponent>, TracConfiguration {

  private static Logger logger = Logger.getInstance(ConfigurationComponent.class.getName());

  static final String COMPONENT_NAME = "IntelliTrac.ConfigurationComponent";

  private static final String DEFAULT_TICKET_TEMPLATES_FILENAME = "default_ticket_templates.yml";
  private static final String DEFAULT_SEND_CODE_POINTER_SETTINGS_FILENAME = "default_send_code_pointer_settings.yml";

  private ConfigurationForm form;

  private ConnectionSettings connectionSettings;
  private DefaultValues defaultValues;
  private List<Template> ticketTemplates;
  private SendCodePointerSettings sendCodePointerSettings;

  public ConfigurationComponent() {
    initializeDefaults();
  }

  private void initializeDefaults() {
    try {
      connectionSettings = new ConnectionSettings();
      defaultValues = new DefaultValues();
      ticketTemplates = Yaml.loadType(getClass().getResourceAsStream("/" + DEFAULT_TICKET_TEMPLATES_FILENAME), ArrayList.class);
      InputStream scpSettings = getClass().getResourceAsStream("/" + DEFAULT_SEND_CODE_POINTER_SETTINGS_FILENAME);
      sendCodePointerSettings = Yaml.loadType(scpSettings, SendCodePointerSettings.class);
    } catch (Exception e) {
      logger.error("Could not load default data from plugin resources. The IntelliTrac plugin is probably damaged.", e);
    }
  }

  public void initComponent() {

  }

  public void disposeComponent() {

  }

  @NotNull
  public String getComponentName() {
    return COMPONENT_NAME;
  }

  public void projectOpened() {

  }

  public void projectClosed() {

  }

  /**
   * Returns the user-visible name of the settings component.
   *
   * @return the visible name of the component.
   */
  @Nls
  public String getDisplayName() {
    return "IntelliTrac";
  }

  /**
   * Returns the icon representing the settings component. Components
   * shown in the IDEA settings dialog have 32x32 icons.
   *
   * @return the icon for the component.
   */
  @Nullable
  public Icon getIcon() {
    return IntelliTracIcons.getInstance().getBigIcon();
  }

  /**
   * Returns the topic in the help file which is shown when help for the configurable
   * is requested.
   *
   * @return the help topic, or null if no help is available.
   */
  @Nullable
  @NonNls
  public String getHelpTopic() {
    return null;  //TODO: Implement
  }

  /**
   * Returns the user interface component for editing the configuration.
   *
   * @return the component instance.
   */
  public JComponent createComponent() {
    if (form == null) {
      form = new ConfigurationForm();
    }
    return form.getRootComponent();
  }

  /**
   * Checks if the settings in the user interface component were modified by the user and
   * need to be saved.
   *
   * @return true if the settings were modified, false otherwise.
   */
  public boolean isModified() {
    return form != null && form.isModified(this);
  }

  /**
   * Store the settings from configurable to other components.
   */
  public void apply() throws ConfigurationException {
    if (form != null) {
      form.getData(this);
    }
    setIntelliTracConfiguration();
  }

  private void setIntelliTracConfiguration() {
    try {
      TracGatewayLocator.retrieveTracGateway().setConfiguration(connectionSettings);
    } catch (MalformedURLException e) {
      TracGatewayLocator.handleConnectionProblem();
    }
  }

  /**
   * Load settings from other components to configurable.
   */
  public void reset() {
    if (form != null) {
      form.setData(this);
    }
  }

  /**
   * Disposes the Swing components used for displaying the configuration.
   */
  public void disposeUIResources() {
    form = null;
  }

  /**
   * @return a component state. All properties and public fields are serialized. Only values, which differ
   *         from default (i.e. the value of newly instantiated class) are serialized.
   * @see com.intellij.util.xmlb.XmlSerializer
   */
  public ConfigurationComponent getState() {
    return this;
  }

  /**
   * This method is called when new component state is loaded. A component should expect this method
   * to be called at any moment of its lifecycle. The method can and will be called several times, if
   * config files were externally changed while IDEA running.
   *
   * @param state loaded component state
   */
  public void loadState(ConfigurationComponent state) {
    if (state.getConnectionSettings() != null) {
      connectionSettings = state.getConnectionSettings();
    }
    if (state.getDefaultValues() != null) {
      defaultValues = state.getDefaultValues();
    }
    if (state.getSendCodePointerSettings() != null) {
      sendCodePointerSettings = state.getSendCodePointerSettings();
    }
    if (state.getTicketTemplates() != null) {
      setTicketTemplates(state.getTicketTemplates());
    }
    try {
      if (connectionSettings != null && !connectionSettings.empty()) {
        TracGatewayLocator.retrieveTracGateway().setConfiguration(connectionSettings);
      }
    } catch (MalformedURLException e) {
      logger.error("Problem loading configuration", e);
    }
  }


  public ConnectionSettings getConnectionSettings() {
    return connectionSettings;
  }

  public void setConnectionSettings(ConnectionSettings connectionSettings) {
    this.connectionSettings = connectionSettings;
  }

  public DefaultValues getDefaultValues() {
    return defaultValues;
  }

  public void setDefaultValues(DefaultValues defaultValues) {
    this.defaultValues = defaultValues;
  }

  public List<Template> getTicketTemplates() {
    return ticketTemplates;
  }

  public void setTicketTemplates(List<Template> ticketTemplates) {
    this.ticketTemplates = DeepCopyUtils.deepCopy(ticketTemplates);
  }

  public SendCodePointerSettings getSendCodePointerSettings() {
    return sendCodePointerSettings;
  }

  public void setSendCodePointerSettings(SendCodePointerSettings sendCodePointerSettings) {
    this.sendCodePointerSettings = sendCodePointerSettings;
  }
}
