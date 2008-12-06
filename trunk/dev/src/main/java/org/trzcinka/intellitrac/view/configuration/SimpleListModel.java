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

package org.trzcinka.intellitrac.view.configuration;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * todo class description
 * <p/>
 * Created on: 2008-12-03 22:42:27 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Michal Trzcinka
 */

public class SimpleListModel<T> extends AbstractListModel {
  private static final Logger log = Logger.getLogger(SimpleListModel.class.getName());

  private List<T> list;

  public SimpleListModel() {
    list = new ArrayList<T>();
  }

  public int getSize() {
    return list.size();
  }

  public T getElementAt(int index) {
    return list.get(index);
  }


  public List<T> getList() {
    return list;
  }

  public void setData(List<T> list) {
    this.list = list;
    fireContentsChanged(this, 0, list.size());
  }

  public void add(T template) {
    list.add(template);
    int index = list.size() - 1;
    fireIntervalAdded(this, index, index);
  }

  public void remove(int selectedIndex) {
    list.remove(selectedIndex);
    fireIntervalRemoved(this, selectedIndex, selectedIndex);
  }
}