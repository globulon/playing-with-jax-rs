package com.promindis.server.bootstrap


import javax.ws.rs.ext._
import javax.xml.bind.JAXBContext

import com.sun.jersey.api.json.{JSONConfiguration, JSONJAXBContext}

import org.eclipse.jetty.server.{Server => JettyServer}
import org.eclipse.jetty.servlet._
import com.sun.jersey.spi.container.servlet.ServletContainer


object WebServer extends App {
  val server = new JettyServer(7777);
  val context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);

  val jerseyServlet = new ServletHolder(classOf[ServletContainer])

  jerseyServlet .setInitParameter("com.sun.jersey.config.property.resourceConfigClass", "com.sun.jersey.api.core.PackagesResourceConfig")
  jerseyServlet .setInitParameter("com.sun.jersey.config.property.packages", "com.promindis.server.bootstrap")
  jerseyServlet .setInitParameter("com.sun.jersey.config.feature.Trace", "true")
  jerseyServlet .setInitParameter("com.sun.jersey.config.feature.Redirect", "true")
  jerseyServlet .setInitParameter("com.sun.jersey.config.feature.Formatted", "true")

  context.setContextPath("/")
  context.addServlet(jerseyServlet, "/*")
  server.setHandler(context)

  server.start()
  server.join()

  @Provider
  class JaxbContextResolver extends ContextResolver[JAXBContext] {
    val paths = "com.promindis.resource"
    val config = JSONConfiguration.mapped().rootUnwrapping(false).build()
    val context = new JSONJAXBContext(config, paths)
    def getContext(clazz : Class[_]) = context
  }
}




