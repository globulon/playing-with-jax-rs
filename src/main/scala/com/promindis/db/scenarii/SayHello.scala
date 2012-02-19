package com.promindis.db.scenarii

import com.promindis.db.adatpers.Database
import org.neo4j.kernel.EmbeddedGraphDatabase
import com.promindis.db.domain.SocialRelationships._

object SayHello extends ((Database[EmbeddedGraphDatabase]) => Unit) {
  def apply(instance: Database[EmbeddedGraphDatabase]) {
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
}
