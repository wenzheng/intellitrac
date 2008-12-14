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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.text.JTextComponent;

/**
 * Validator that checks if given text component is not empty.
 * <p/>
 * Created on: 2008-11-16 22:33:33 <br/>
 *
 * @author Michal Trzcinka
 */

public class NotEmptyValidator extends Validator {
  private static final Logger log = Logger.getLogger(NotEmptyValidator.class.getName());

  public NotEmptyValidator(GroupValidator groupValidator, JComponent component, String message) {
    super(groupValidator, component, message);
  }

  protected boolean validationCriteria(JComponent c) {
    String text = ((JTextComponent) c).getText();
    return !StringUtils.isEmpty(text);
  }

}