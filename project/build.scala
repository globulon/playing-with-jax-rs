import sbt._
import Keys._


object BuildSettings {
  val buildSettings = Defaults.defaultSettings ++ Seq (
    organization        := "com.promindis",
    version             := "0.1-SNAPSHOT",
    scalaVersion        := "2.9.1",
    scalacOptions       := Seq("-unchecked", "-deprecation")
  )
}


object Resolvers {
  val typesafeReleases = "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"
  val scalaToolsRepo = "sonatype-oss-public" at "https://oss.sonatype.org/content/groups/public/"
}


object Versions {
  val Jersey = "1.9.1"
  val Jetty  = "8.0.4.v20111024"
  val Specs2Version = "1.7.1"
}

object SourceDependencies {
  import Versions._

  lazy val jsr311       = "javax.ws.rs"       % "jsr311-api"      % "1.1.1" % "compile"
  lazy val jerseyCore   = "com.sun.jersey"    % "jersey-core"     % Jersey  % "compile"
  lazy val jerseyJson   = "com.sun.jersey"    % "jersey-json"     % Jersey  % "compile"
  lazy val jerseyServer = "com.sun.jersey"    % "jersey-server"   % Jersey  % "compile"
  lazy val jettyServer  = "org.eclipse.jetty" % "jetty-server"    % Jetty   % "compile"
  lazy val jettyServlet = "org.eclipse.jetty" % "jetty-servlet"   % Jetty   % "compile"
  lazy val jettyWebapp  = "org.eclipse.jetty" % "jetty-webapp"    % Jetty   % "compile"

}

object TestDependencies {
  import Versions._  
  val testDependencies = "org.specs2" %% "specs2" % Specs2Version % "test"
}


object MainBuild extends Build {
  import Resolvers._
  import SourceDependencies._
  import TestDependencies._
  import BuildSettings._

  lazy val algorithms = Project(
    "playing with jax-rs",
    file("."),
    settings = buildSettings ++ Seq (
      resolvers            := Seq (typesafeReleases, scalaToolsRepo),
      libraryDependencies ++= Seq (jsr311, jerseyCore, jerseyJson, jerseyServer),
      // container dependencies
      libraryDependencies ++= Seq (jettyServer, jettyServlet, jettyWebapp),
      // runtime dependencies
      libraryDependencies ++= Seq (testDependencies)
    )
  )
}

