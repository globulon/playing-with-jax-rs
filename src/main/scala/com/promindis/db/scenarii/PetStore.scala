package com.promindis.db.scenarii

import com.promindis.db.adatpers.Database
import com.promindis.db.domain.PetStoreRelationships._
import com.promindis.db.adatpers.DatabaseDecorators._
import com.promindis.db.domain.PetStoreProperties._
import org.neo4j.kernel.{Uniqueness, Traversal, EmbeddedGraphDatabase}
import org.neo4j.graphdb.Path
import org.neo4j.graphdb.traversal.{Evaluation, Evaluator}
import scala.collection.JavaConverters._
import Uniqueness._
import Evaluation._
import Traversal._

/**
 * Date: 19/02/12
 * Time: 13:28
 */

object PetStore extends ((Database[EmbeddedGraphDatabase]) => Unit) {
  def apply(instance: Database[EmbeddedGraphDatabase]) {
    implicit val db = instance
    val p1 = instance.createNode(_.withProperty(NAME, "Principal1"))
    val p2 = instance.createNode(_.withProperty(NAME, "Principal2"))
    val pet0 = instance.createNode(_.withProperty(NAME, "Pet0"))
    val pet1 = instance.createNode(_.withProperty(NAME, "Pet1"))
    val pet2 = instance.createNode(_.withProperty(NAME, "Pet2"))
    val pet3 = instance.createNode(_.withProperty(NAME, "Pet3"))

    List(pet1, pet2, pet3).foreach{pet0.relatesTo(_, DESCENDANT)}

    p1.relatesTo(pet1, OWNS)
    p1.relatesTo(pet2, OWNS)

    p2.relatesTo(pet3, OWNS)

    description()
      .uniqueness(NODE_PATH).evaluator(new Evaluator {
      def evaluate(path: Path) = {
        if (path.endNode() == p1.delegate) INCLUDE_AND_PRUNE
        else EXCLUDE_AND_CONTINUE
      }
    }).traverse(pet0).asScala.foreach{println(_)}

  }
}
