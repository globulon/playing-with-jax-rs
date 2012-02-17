package com.promindis.application

import com.promindis.db.bootstrap.DbServer._
import com.promindis.db.bootstrap.Relationships._
import java.nio.file.Paths

/**
 * Date: 17/02/12
 * Time: 21:49
 */


object Main extends App {

  val db = createEmbeddedDb()
  db {
    instance =>
      val firstNode = instance.createNode()
      firstNode.setProperty("message", "Hello")
      val secondNode = instance.createNode()
      secondNode.setProperty("message", "World !")
      val relationship = firstNode.createRelationshipTo(secondNode, KNOWS);
      relationship.setProperty("message", "brave Neo4j ");

      println(firstNode.getProperty("message"))
      println(relationship.getProperty("message"))
      println(secondNode.getProperty("message"))
  }

  db.shutdown()


}
