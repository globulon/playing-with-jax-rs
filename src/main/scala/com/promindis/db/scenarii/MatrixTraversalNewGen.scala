package com.promindis.db.scenarii

import com.promindis.db.adatpers.Database
import com.promindis.db.domain.UserProperties._
import com.promindis.db.domain.SocialRelationships._
import com.promindis.db.domain.MatrixRelationships
import MatrixRelationships._
import com.promindis.db.domain.SoftwareProperties._
import org.neo4j.graphdb.Direction
import Direction._
import com.promindis.db.adatpers.DatabaseDecorators._
import scala.collection.JavaConverters._
import org.neo4j.kernel.{Traversal, EmbeddedGraphDatabase}
import org.neo4j.graphdb.traversal.Evaluators


object MatrixTraversalNewGen extends ((Database[EmbeddedGraphDatabase]) => Unit) {
  def apply(instance: Database[EmbeddedGraphDatabase]) {
    implicit val db = instance
    val neo = instance.createNode(_.withProperty(USER_NAME, "neo"))
    instance.referenceNode.createRelationshipTo(neo, MatrixRelationships.THE_ONE)
    val trinity = neo.relationToNew(USER_NAME, "trinity", KNOWS)

    neo
      .relationToNew(USER_NAME, "morpheus", KNOWS)
      .withProperties((USER_OCCUPATION, "Total Badass"), (USER_RANK, "captain"))
      .relatesTo(trinity, KNOWS)
      .relationToNew(USER_NAME, "Cypher", KNOWS).withProperties((USER_LAST_NAME, "Reagan"))
      .relationToNew(USER_NAME, "AgentSmith", KNOWS).withProperties((VERSION, "1.0"), (LANGUAGE, "C++"))
      .relationToNew(USER_NAME, "Architect", CODED_BY)

    Traversal.description()
      .breadthFirst()
      .relationships(CODED_BY, OUTGOING)
      .relationships(KNOWS, OUTGOING)
      .evaluator(Evaluators.returnWhereLastRelationshipTypeIs(CODED_BY))
      .traverse(neo).asScala.foreach {
      println(_)
    }

  }
}
