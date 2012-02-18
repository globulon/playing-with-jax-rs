package com.promindis.db.domain

import org.neo4j.graphdb.RelationshipType

/**
 * Date: 17/02/12
 * Time: 21:23
 */

sealed trait Relationship extends RelationshipType {
  final override def name = toString
}

object SocialRelationships {
  sealed trait SocialNetworkRelationship extends Relationship
  case object KNOWS extends SocialNetworkRelationship
}

object UserRelationships {
  sealed trait UserRelationship extends Relationship
  case object USER_REFERENCE extends UserRelationship
  case object USER extends UserRelationship

}

