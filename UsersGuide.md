# Table Of Contents #

# What is IntelliTrac #

IntelliTrac is an open-source IntelliJ IDEA plugin that enables you to manage Trac environments from within the IDE. It requires Trac 0.11 environment with XML-RPC plugin installed. See blabla

# Installation & configuration #

## Requirements ##

  * IntelliJ IDEA 7/8
  * Trac (at least 0.10) with XML-RPC plugin

## XML-RPC plugin ##

If you don't have XML-RPC plugin installed, please visit http://trac-hacks.org/wiki/XmlRpcPlugin#Installation for installation manual. The installation procedure is very simple.

**Important:** when you download the zipped source of the plugin, you will discover there are three directories: 0.10, sandbox and trunk. Please install the plugin from trunk directory - there may appear some problems with 0.10 while running Trac on Python < 2.5.

To ensure the plugin is up and running, launch http://your_trac_environment/xmlrpc in your web browser. You should see a list of available XML-RPC procedures.

# Key features #

## Reports management ##

With IntelliTrac you may define several ticket reports (analoguous to reports in web interface). IntelliTrac comes with 4 predefined reports:
  * all tickets
  * all active tickets
  * my all tickets
  * my all active tickets

You may define your own reports and remove predefined ones as well. Every report is validated upon update/creation, so there is no threat of saving invalid report queries.
Please refer to TracQuery site (http://trac.edgewall.org/wiki/TracQuery) for specific information on query syntax.

## Tickets management ##

After choosing report, you can list tickets by clicking **open** or double-clicking the report name. You can sort them (by clicking appriopriate column headers) and filter them as well. To view the ticket, double-click it or choose **edit** from the top menu. To create a new ticket, click the green oval with plus in the top menu.

Ticket editing/creating delivers the same functionality as the web browser interface. Moreover, fields that accept TracWiki syntax (i.e. description and comment) enable the **preview** mode. For example, type {{{
''hello'' }}}
in the description field, and check **preview** radio button. You should see "hello" word formatted according to the TracWiki syntax.

## Connection settings ##

IntelliTrac has a per-project configuration. Therefore, you can define different Trac environments for different projects. You should provide those settings by opening Project settings => IntelliTrac => Connection.

## Default values ##

You can define default values for some ticket fields, such as priority, component or milestone. Open Project settings => IntelliTrac => Default Values and choose proper values from combo boxes. From now on, every time you create a new ticket it will contain those predefined values.

## Ticket templates ##

When creating a new ticket, you choose a ticket template to use. You may define several templates by opening Project settings => IntelliTrac => Ticket templates. When you choose "create ticket" (green button in top menu):
  * when there are no templates, IntelliTrac simply creates an empty ticket
  * when there is only one template, IntelliTrac simply uses it
  * when there is more than one template, you are asked to choose the template you would like to use

## Send code pointer ##

While using any editor, you may send code pointer to the currently selected ticket description or comment. Right-click any area in the editor, choose "Send code pointer to Trac ticket" and select "To description" or "To comment". IntelliTrac pastes proper pointer. If you have any text selected in the editor, this selection will be pasted also.

**Send code pointer** option is enabled only if you are editing/creating a ticket. You may modify **send code pointer** templates by opening Project settings => IntelliTrac => "Send code pointer" Templates.