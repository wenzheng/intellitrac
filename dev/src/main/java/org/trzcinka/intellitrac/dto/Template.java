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

package org.trzcinka.intellitrac.dto;

import org.apache.log4j.Logger;
import org.trzcinka.intellitrac.utils.Copiable;

/**
 * todo class description
 * <p/>
 * Created on: 2008-11-29 23:44:42 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Michal Trzcinka
 */

public class Template implements Copiable<Template> {
  private static final Logger log = Logger.getLogger(Template.class.getName());

  private String name;
  private String content;

  public Template() {
  }

  public Template(String content) {
    this.content = content;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Template template = (Template) o;

    if (content != null ? !content.equals(template.content) : template.content != null) return false;
    if (name != null ? !name.equals(template.name) : template.name != null) return false;

    return true;
  }

  public Template deepCopy() {
    Template result = new Template();
    result.setContent(content);
    result.setName(name);
    return result;
  }

}