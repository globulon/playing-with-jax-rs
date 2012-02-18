package com.promindis.db.adatpers

import com.promindis.db.adatpers.DatabaseTools.{DatabaseAccessor, DatabaseLifeCycleManager}
import org.neo4j.graphdb.index.Index
import org.neo4j.graphdb.{Node, GraphDatabaseService}

/**
 * Date: 18/02/12
 * Time: 14:34
 */

case class Database[T <: GraphDatabaseService : DatabaseLifeCycleManager : DatabaseAccessor](instance: T, path: String) {
  self =>

  lazy val lifeCycleManager = implicitly[DatabaseLifeCycleManager[T]]

  lazy val accessor = implicitly[DatabaseAccessor[T]]

  def setup()   = {
    lifeCycleManager.setup(instance)
    self
  }
  def apply(f: Database[T] => Unit) = {accessor.apply(this)(f)}

  def shutdown() { lifeCycleManager.shutdown(instance)(path)}

  def newIndexedNode[K](key: K, name: String)(implicit index: Index[Node], f: K => String) =
    accessor.newIndexedNode(instance, key, name)

  def foundNode[K](key: K, searchedCriteria: Any)(implicit index: Index[Node], f: K => String): Node =
    accessor.foundNode(key, searchedCriteria)

  def indexFor(name: String) = instance.index().forNodes(name)

  def createNode() = instance.createNode()

  def createNode(f: Node => Node) = f(instance.createNode())

  def referenceNode = instance.getReferenceNode
}