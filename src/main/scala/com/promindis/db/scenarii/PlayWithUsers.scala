package com.promindis.db.scenarii

import com.promindis.db.adatpers.Database
import org.neo4j.kernel.EmbeddedGraphDatabase
import com.promindis.db.domain.UserRelationships._
import com.promindis.db.domain.UserProperties._
import com.promindis.db.adatpers.DatabaseDecorators._

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
