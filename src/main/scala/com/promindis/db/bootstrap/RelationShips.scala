package com.promindis.db.bootstrap

import org.neo4j.graphdb.RelationshipType

/**
 * Date: 17/02/12
 * Time: 21:23
 */

object Relationships{
  trait Relationship extends RelationshipType

  object KNOWS extends Relationship{def name() = "KNOWS"}
}

