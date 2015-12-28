# Project goals #

IntelliTrac is an IntelliJ IDEA plugin (aimed at version 7) that enables tight integration with Trac, a popular project management system. Its main aim is to facilitate Trac support, i.e. enabling users to interface with Trac environments from within IDE (no need to use Trac web browser interface).
The first version of the plugin will offer rather modest functionality:
  * managing tickets (opening, closing, editing etc.)

# Trac integration #

The plugin will communicate with Trac via XML-RPC (http://www.xmlrpc.com/). It requires that Trac server must have Trac XML-RPC Plugin installed (http://trac-hacks.org/wiki/XmlRpcPlugin).
However XML-RPC is a deprecated protocol (SOAP is more common nowadays), it still seems to be a better solution than manually parsing HTML pages using a pure HTTP client.

# Technical issues #

The development includes following:
  * Java Development Kit 6 update 7
  * IntelliJ IDEA Software Development Kit 7757

The project will be developed under IntelliJ IDEA 7 using Plugin Development Package (http://www.jetbrains.com/idea/plugins/plugin_developers.html).

See also LibrariesAndFrameworks.

# Useful links #

  * http://ws.apache.org/xmlrpc/index.html
  * http://trac-hacks.org/wiki/XmlRpcPlugin
  * http://www.jetbrains.com/idea/plugins/plugin_developers.html