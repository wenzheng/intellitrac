# Main panel #

This panel will be visible within IDE by clicking a button on the IDE toolbar (just like "Ant", "Maven projects" or "TODO").

![http://intellitrac.googlecode.com/svn/trunk/doc/img/tickets_flow.png](http://intellitrac.googlecode.com/svn/trunk/doc/img/tickets_flow.png)

## Toolbar ##
There will be a toolbar at the top and a list of reports at the bottom. It will be always visible, however it will change its content depending on the current context. It will always contain navigation buttons (home ([reports list](#Reports_list.md)), back, [new ticket](#Ticket_editor.md)) in a constant place, e.g. right-aligned.

## Reports list ##
It will be the first state. Toolbar will contain buttons:

  * Open report (only active if a report is selected in the list) - navigates to [list of tickets](#Tickets_list.md).
  * Add new report - navigates to [report editor](#Report_editor.md).
  * Edit report (only active if a report is selected in the list) - navigates to [report editor](#Report_editor.md).
  * Remove report (only active if a report is selected in the list) - removes selected report (possibly with a prompt).

## Tickets list ##

Tickets list will be a table. Every ticket will be described by:
  * number
  * summary
  * type
  * owner
  * status

Buttons in the toolbar:

  * Start working on a ticket - registers that the user has just started working on this ticket, starting a timer. It will be active if all these conditions are met:
    * the user is not working on any other ticket yet,
    * there is a selected ticket in the list.

  * Stop working on a ticket - updates this ticket, adding calculated time. It will be active if all these conditions are met:
    * there is a selected ticket in the list,
    * the user started working on this ticket (by clicking "start working on a ticket").

  * Pause working on a ticket - pauses the timer for this ticket working time, but does not stop it - the user is still working on this ticket. Required conditions:
    * there is a selected ticket in the list,
    * the user started working on this ticket (by clicking "start working on a ticket").

  * Edit ticket - navigates to [ticket editor](#Ticket_editor.md).

## Report editor ##

  * Title
  * Description
  * Query

## Ticket editor ##

Analogous to the web ticket editor.

# Settings #

This panel will be accessible by clicking "IntelliTrac settings" in Project settings. It will be just a form with following fields:

  * Connection
    * Trac URL - text field - direct URL to the Trac project (concrete project, e.g.          http://my-track.com/project).
    * Login
    * Password
    * button Test connection
  * Default values
    * Component
    * Priority
    * Type
    * Milestone
    * Version
  * Ticket templates
    * list of editable ticket templates
  * Send code pointer templates
    * with selection
    * only caret

OK and Cancel buttons underneath.