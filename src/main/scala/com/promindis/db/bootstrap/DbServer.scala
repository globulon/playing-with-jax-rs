package com.promindis.db.bootstrap

import org.neo4j.kernel.EmbeddedGraphDatabase
import DatabaseTools._
import org.neo4j.graphdb.GraphDatabaseService

/**
 * Date: 17/02/12
 * Time: 21:14
 */



object DbServer {
  lazy val PATH = "neo-db"

  implicit object EmbeddedDatabaseLifeCycleManager extends DatabaseLifeCycleManager[EmbeddedGraphDatabase]
  implicit object EmbeddedDatabaseAccessor extends DatabaseAccessor[EmbeddedGraphDatabase] with DatabaseTransactionManager[EmbeddedGraphDatabase]

  def onSteroids[T <: GraphDatabaseService : DatabaseLifeCycleManager : DatabaseAccessor](db: T, path: String) = new {
    self =>

    lazy val lifeCycleManager = implicitly[DatabaseLifeCycleManager[T]]
    lazy val accessor = implicitly[DatabaseAccessor[T]]

    def setup() = {
      lifeCycleManager.setup(db)
      self
    }

    def apply(f: (T) => Unit) {accessor.apply(db)(f)}

    def shutdown() { lifeCycleManager.shutdown(db)(path)}
  }

  def createEmbeddedDb() = onSteroids(new EmbeddedGraphDatabase(PATH), PATH).setup()


}
