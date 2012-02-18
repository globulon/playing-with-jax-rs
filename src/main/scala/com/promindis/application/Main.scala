package com.promindis.application

import com.promindis.db.bootstrap.DbServer._

import com.promindis.db.domain.SocialRelationships._
import org.neo4j.kernel.EmbeddedGraphDatabase
import org.neo4j.graphdb.index.{Index}
import org.neo4j.graphdb.Node
import com.promindis.db.domain.UserProperties._
import com.promindis.db.domain.{UserRelationships, Property}
import UserRelationships._

/**
 * Date: 17/02/12
 * Time: 21:49
 */

object SayHello extends ((EmbeddedGraphDatabase) => Unit) {
  def apply(instance: EmbeddedGraphDatabase) {
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

object PlayWithUsers extends ((EmbeddedGraphDatabase) => Unit) {

  def apply(instance: EmbeddedGraphDatabase) {
    import Property._

    def namedFrom(id: Long) = "user" + id + "@neo4j"

    def indexedUser[T <: Property](key: T, name: String)(implicit index: Index[Node], f: T => String) = {
      val node = instance.createNode()
      node.setProperty(key, name)
      index.add(node, key, name)
      node
    }

    def foundUser[T <: Property](key: T, searchedCriteria: Any)(implicit index: Index[Node], f: T => String): Any = {
      index.get(key, searchedCriteria).getSingle
    }

    implicit val index = instance.index().forNodes("nodes")

    val usersReference = instance.createNode()
    instance.getReferenceNode.createRelationshipTo(usersReference, USER_REFERENCE)

    for (i <- 1L to 100L) {
      usersReference.createRelationshipTo(indexedUser(USER_NAME, namedFrom(i)), USER)
    }

    for (id <- 1L to 100L) {
      println(foundUser(USER_NAME, namedFrom(id)))
    }

  }
}

object Main extends App {

  val db = createEmbeddedDb()

  db {SayHello}

  db {PlayWithUsers}

  db.shutdown()
}
