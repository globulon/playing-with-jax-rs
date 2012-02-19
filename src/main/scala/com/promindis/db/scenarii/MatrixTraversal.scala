package com.promindis.db.scenarii

import com.promindis.db.adatpers.Database
import org.neo4j.kernel.EmbeddedGraphDatabase
import com.promindis.db.domain.UserProperties._
import com.promindis.db.domain.SocialRelationships._
import com.promindis.db.domain.MatrixRelationships
import MatrixRelationships._
import com.promindis.db.domain.SoftwareProperties._
import org.neo4j.graphdb.Traverser.Order
import Order._
import org.neo4j.graphdb.{Direction, ReturnableEvaluator, StopEvaluator}
import Direction._
import StopEvaluator._
import ReturnableEvaluator._
import com.promindis.db.adatpers.DatabaseDecorators._
import scala.collection.JavaConverters._


object MatrixTraversal extends ((Database[EmbeddedGraphDatabase]) => Unit) {
  def apply(instance: Database[EmbeddedGraphDatabase]) {
    implicit val db = instance
    val neo = instance.createNode(_.withProperty(USER_NAME, "neo"))
    instance.referenceNode.createRelationshipTo(neo, THE_ONE)
    val trinity = neo.relationToNew(USER_NAME, "trinity", KNOWS)

    neo
      .relationToNew(USER_NAME, "morpheus", KNOWS)
      .withProperties((USER_OCCUPATION, "Total Badass"), (USER_RANK, "captain"))
      .relatesTo(trinity, KNOWS)
      .relationToNew(USER_NAME, "Cypher", KNOWS).withProperties((USER_LAST_NAME, "Reagan"))
      .relationToNew(USER_NAME, "AgentSmith", KNOWS).withProperties((VERSION, "1.0"), (LANGUAGE, "C++"))
      .relationToNew(USER_NAME, "Architect", CODED_BY)

    for (node <- neo.traverse(BREADTH_FIRST, END_OF_GRAPH, ALL_BUT_START_NODE, KNOWS, OUTGOING).asScala) {
      println(node.description)
    }

  }
}
