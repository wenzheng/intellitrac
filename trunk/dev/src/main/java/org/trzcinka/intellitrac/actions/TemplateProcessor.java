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

package org.trzcinka.intellitrac.actions;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.trzcinka.intellitrac.dto.Template;

/**
 * todo class description
 * <p/>
 * Created on: 2008-12-06 20:57:58 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Michal Trzcinka
 */

public class TemplateProcessor {
  private static final Logger log = Logger.getLogger(TemplateProcessor.class.getName());

  public static String processTemplate(Template template, TemplateParams params) {
    String templateText = template.getContent();
    String withFileReplaced = StringUtils.replace(templateText, TemplateParams.FILE_NAME, params.getFileName());
    String withLineReplaced = StringUtils.replace(withFileReplaced, TemplateParams.LINE, String.valueOf(params.getLine()));
    return StringUtils.replace(withLineReplaced, TemplateParams.SELECTION, String.valueOf(params.getSelection()));
  }

}