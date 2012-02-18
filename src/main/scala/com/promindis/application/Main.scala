package com.promindis.application


import com.promindis.db.domain.SocialRelationships._
import org.neo4j.kernel.EmbeddedGraphDatabase
import com.promindis.db.domain.UserProperties._
import com.promindis.db.domain.{UserRelationships, Property}
import UserRelationships._
import com.promindis.db.adatpers.DatabaseFactory._
import com.promindis.db.adatpers.Database

/**
 * Date: 17/02/12
 * Time: 21:49
 */

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

object PlayWithUsers extends ((Database[EmbeddedGraphDatabase]) => Unit) {

  def apply(instance: Database[EmbeddedGraphDatabase]) {
    import Property._

    def namedFrom(id: Long) = "user" + id + "@neo4j"

    implicit val index = instance.indexFor("nodes")

    val usersReference = instance.createNode()
    instance.referenceNode.createRelationshipTo(usersReference, USER_REFERENCE)

    for (i <- 1L to 100L) {
      usersReference.createRelationshipTo(instance.newIndexedNode(USER_NAME, namedFrom(i)), USER)
    }

    for (id <- 1L to 100L) {
      println(instance.foundNode(USER_NAME, namedFrom(id)))
    }

  }
}

object Main extends App {

  val db = createEmbeddedDb()

  db {SayHello}

  db {PlayWithUsers}

  db.shutdown()
}
