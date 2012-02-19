package com.promindis.db.domain

import org.neo4j.graphdb.RelationshipType

/**
 * Date: 17/02/12
 * Time: 21:23
 */

sealed trait Relationship extends RelationshipType {
  final override def name = toString
}

object MatrixRelationships {
  sealed trait MatrixNetworkRelationship extends Relationship
  case object THE_ONE extends MatrixNetworkRelationship
  case object CODED_BY extends MatrixNetworkRelationship
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

object PetStoreRelationships {
  sealed trait PetStoreRelationship extends Relationship
  case object OWNERS extends PetStoreRelationship
  case object OWNER extends PetStoreRelationship
  case object OWNS extends PetStoreRelationship
  case object PETS extends PetStoreRelationship
  case object PET extends PetStoreRelationship
  case object DESCENDANT extends PetStoreRelationship
}

