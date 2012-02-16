package com.promindis.server.bootstrap

import org.eclipse.jetty.server.{Server => JettyServer}
import org.eclipse.jetty.servlet._
import com.sun.jersey.spi.container.servlet.ServletContainer
import com.sun.jersey.api.core.PackagesResourceConfig
import ServletContextHandler._

object WebServer extends App {

  def properties: java.util.Map[String, AnyRef] = {
    new java.util.HashMap[String, AnyRef] {
      put("com.sun.jersey.config.property.packages", "com.promindis.server.bootstrap;com.promindis.resources")
      put("com.sun.jersey.config.feature.Trace", "true")
      put("com.sun.jersey.config.feature.Redirect", "true")
      put("com.sun.jersey.config.feature.Formatted", "true")
    }
  }

  implicit def onSteroids(contextHandler: ServletContextHandler) = new {
    def withContext(path: String) = {
      contextHandler.setContextPath(path)
      contextHandler
    }

    def holderFor(instance: ServletHolder, path: String) = {
      contextHandler.addServlet(instance, path)
      contextHandler
    }
  }

  implicit def onSteroids(server: JettyServer) = new {
    self =>

    def contextHandler: ServletContextHandler = {
      new ServletContextHandler(NO_SESSIONS)
    }

    def initialized(f: ServletContextHandler => Unit) = {
      val handler = contextHandler
      f(handler)
      server.setHandler(handler)
      self
    }

    def boot() = {
      server.start()
      server.join()
      self
    }
  }

  def jerseyServlet = new ServletHolder(new ServletContainer(new PackagesResourceConfig(properties)))

  new JettyServer(7777)
    .initialized{ _.withContext("/").holderFor(jerseyServlet, "/*")}
      .boot()

}




