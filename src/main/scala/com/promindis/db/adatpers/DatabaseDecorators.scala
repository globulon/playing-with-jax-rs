package com.promindis.db.adatpers

import com.promindis.db.domain.Property
import org.neo4j.graphdb.{Relationship, RelationshipType, Node}

/**
 * Date: 18/02/12
 * Time: 15:23
 */

object  DatabaseDecorators {
  implicit def onSteroids(n: Node) = NodeOnSteroids(n)

  implicit def lite(node: NodeOnSteroids): Node = node.delegate
}


final case class NodeOnSteroids(delegate: Node) {
  import DatabaseDecorators._

  def withProperties[T <: Property <% String](list: (T, String)*)= {
    for ((key, value) <- list)
      delegate.setProperty(key, value)
    this
  }

  def withProperty[T <% String](key: T, value: Any) = {
    delegate.setProperty(key, value)
    this
  }

  def relationToNew[T, R <: RelationshipType](key: T, value: Any,relation: R )(implicit db: Database[_], t: T => String): NodeOnSteroids = {
    val newNode = db.createNode(_.withProperty(key, value))
    delegate.createRelationshipTo(newNode, relation)
    newNode
  }

  def relatedTo[R <: RelationshipType](other: Node, withRelationType: R, f: Relationship => Unit = (_ => {})) = {
    delegate.createRelationshipTo(other.delegate, withRelationType)
    this
  }

  def description  = {
    import scala.collection.JavaConverters._
    "[" + delegate.getId +  "] " +
      delegate.getPropertyKeys.asScala.map{
      item => (item , delegate.getProperty(item))
      }.mkString(",")
  }
}

