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

package org.trzcinka.intellitrac.view.validation;

import com.intellij.openapi.diagnostic.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Base class for validators.
 * <p/>
 * Created on: 2008-11-16 22:31:29 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Michal Trzcinka
 */

public abstract class Validator extends InputVerifier implements KeyListener {

  private static Logger log = Logger.getInstance(Validator.class.getName());

  private JDialog popup;
  private JLabel messageLabel;
  private Point point;
  private Dimension cDim;
  private Color color;

  private GroupValidator groupValidator;
  private Component validatedComponent;

  /**
   * @param groupValidator     may be null. Group validator will receive notifications on validation status (e.g. a parent form may need it to disable OK button).
   * @param validatedComponent The JComponent to be validated.
   * @param message            A message to be displayed in the popup help tip if validation fails.
   */

  public Validator(GroupValidator groupValidator, Component validatedComponent, String message) {
    this.groupValidator = groupValidator;
    this.validatedComponent = validatedComponent;

    color = new Color(243, 255, 159);
    validatedComponent.addKeyListener(this);
    messageLabel = new JLabel(message);
    popup = new JDialog();
    initComponents();
  }

  /**
   * Implement the actual validation logic in this method. The method should
   * return false if data is invalid and true if it is valid. It is also possible
   * to set the popup message text with setMessage() before returning, and thus
   * customize the message text for different types of validation problems.
   *
   * @param c The JComponent to be validated.
   * @return false if data is invalid. true if it is valid.
   */

  protected abstract boolean validationCriteria(JComponent c);

  /**
   * This method is called by Java when a component needs to be validated.
   * It should not be called directly. Do not override this method unless
   * you really want to change validation behavior. Implement
   * validationCriteria() instead.
   */

  public boolean verify(JComponent c) {

    if (!validationCriteria(c)) {

      groupValidator.validationFailed(this);
      c.setBackground(Color.PINK);
      popup.setSize(0, 0);
      popup.setLocationRelativeTo(c);
      point = popup.getLocation();
      cDim = c.getSize();
      popup.setLocation(point.x - (int) cDim.getWidth() / 2,
        point.y + (int) cDim.getHeight() / 2);
      popup.pack();
      popup.setVisible(true);
      return false;
    }

    c.setBackground(Color.WHITE);
    groupValidator.validationSucceeded(this);

    return true;
  }

  /**
   * Changes the message that appears in the popup help tip when a component's
   * data is invalid. Subclasses can use this to provide context sensitive help
   * depending on what the user did wrong.
   *
   * @param message
   */

  protected void setMessage(String message) {
    messageLabel.setText(message);
  }

  /**
   * @see KeyListener
   */

  public void keyPressed(KeyEvent e) {
    validationDisposed();
  }

  /**
   * @see KeyListener
   */

  public void keyTyped(KeyEvent e) {
  }

  /**
   * @see KeyListener
   */

  public void keyReleased(KeyEvent e) {
  }

  private void initComponents() {
    popup.getContentPane().setLayout(new FlowLayout());
    popup.setUndecorated(true);
    popup.getContentPane().setBackground(color);
    popup.getContentPane().add(messageLabel);
    popup.setFocusableWindowState(false);
  }

  public void validationDisposed() {
    validatedComponent.setBackground(UIManager.getColor("TextField.background"));
    popup.setVisible(false);
  }

}