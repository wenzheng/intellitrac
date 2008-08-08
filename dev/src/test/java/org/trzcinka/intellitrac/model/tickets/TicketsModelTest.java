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

package org.trzcinka.intellitrac.model.tickets;

import org.testng.annotations.Test;

@Test
public class TicketsModelTest {

  public void testStateChanges() {
    TicketsModel model = TicketsModel.getInstance();
    SimpleTicketsStateChangeListener listener = new SimpleTicketsStateChangeListener();
    model.addStateListener(listener);

    assert listener.getTicketsStateInfo() == null;
    model.goBack();
    assert listener.getTicketsStateInfo() == null;

    TicketsStateInfo reportEditor = new TicketsStateInfo(TicketsState.REPORT_EDITOR, null);
    model.setCurrentState(reportEditor);
    assert listener.getTicketsStateInfo() == reportEditor;

    TicketsStateInfo ticketList = new TicketsStateInfo(TicketsState.TICKETS_LIST, null);
    model.setCurrentState(ticketList);
    assert listener.getTicketsStateInfo() == ticketList;

    model.goBack();
    assert listener.getTicketsStateInfo() == reportEditor;
  }

  private class SimpleTicketsStateChangeListener implements TicketsStateChangeListener {
    private TicketsStateInfo ticketsStateInfo;

    public void stateChanged(TicketsStateInfo ticketsStateInfo) {
      this.ticketsStateInfo = ticketsStateInfo;
    }

    public TicketsStateInfo getTicketsStateInfo() {
      return ticketsStateInfo;
    }
  }

}