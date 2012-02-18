package com.promindis.application


import com.promindis.db.domain.SocialRelationships._
import org.neo4j.kernel.EmbeddedGraphDatabase
import com.promindis.db.domain.UserProperties._
import com.promindis.db.adatpers.DatabaseFactory._
import com.promindis.db.adatpers.{DatabaseDecorators, Database}
import DatabaseDecorators._
import com.promindis.db.domain.{SoftwareProperties, UserRelationships, MatrixRelationships, Property}
import Property._
import MatrixRelationships._
import UserRelationships._
import SoftwareProperties._
import org.neo4j.graphdb.Traverser.Order
import org.neo4j.graphdb.{Direction, ReturnableEvaluator, StopEvaluator}
import Order._
import Direction._
import StopEvaluator._
import ReturnableEvaluator._
import scala.collection.JavaConverters._
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
    def namedFrom(id: Long) = "user" + id + "@neo4j"
    implicit val index = instance.indexFor("nodes")

    val usersReference = instance.createNode()
    instance.referenceNode.createRelationshipTo(usersReference, USER_REFERENCE)

    for (i <- 1L to 100L)
      usersReference.createRelationshipTo(instance.newIndexedNode(USER_NAME, namedFrom(i)), USER)

    for (id <- 1L to 100L)
      println(instance.foundNode(USER_NAME, namedFrom(id)).description)
  }
}

object Matrix extends ((Database[EmbeddedGraphDatabase]) => Unit) {
  def apply(instance: Database[EmbeddedGraphDatabase]) {
    implicit val db = instance
    val neo = instance.createNode(_.withProperty(USER_NAME, "neo"))
    instance.referenceNode.createRelationshipTo(neo, MatrixRelationships.THE_ONE)
    val trinity = neo.relationToNew(USER_NAME, "trinity", KNOWS)

    neo
    .relationToNew(USER_NAME, "morpheus", KNOWS)
      .withProperties((USER_OCCUPATION, "Total Badass"), (USER_RANK, "captain"))
      .relatedTo(trinity, KNOWS)
    .relationToNew(USER_NAME, "Cypher", KNOWS).withProperties((USER_LAST_NAME, "Reagan"))
    .relationToNew(USER_NAME, "AgentSmith", KNOWS).withProperties((VERSION, "1.0"), (LANGUAGE, "C++"))
    .relationToNew(USER_NAME, "Architect", CODED_BY)

    for (node <- neo.traverse(BREADTH_FIRST, END_OF_GRAPH, ALL_BUT_START_NODE, KNOWS, OUTGOING).asScala) {
      println(node.description)
    }

  }
}

object Main extends App {

  val db = createEmbeddedDb()

  db {SayHello}

  db {PlayWithUsers}

  db {Matrix}

  db.shutdown()
}
