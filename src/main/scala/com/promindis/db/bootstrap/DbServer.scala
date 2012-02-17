package com.promindis.db.bootstrap

import org.neo4j.kernel.EmbeddedGraphDatabase
import Runtime._
import org.neo4j.graphdb.GraphDatabaseService
import org.neo4j.kernel.impl.util.FileUtils._
import java.nio.file.Paths._

/**
 * Date: 17/02/12
 * Time: 21:14
 */

trait DatabaseLifeCycleManager[T <: GraphDatabaseService] {
  def setup(db: T) {
    getRuntime.addShutdownHook(new Thread() {
      override def run() {db.shutdown()}
    })
  }

  def shutdown(db: T)(path: String) {
    db.shutdown()
    deleteRecursively(get(path).toFile)
  }
}

trait DatabaseTransactionManager[T <: GraphDatabaseService] {
  def inTx[R](db: T)(f: (T) => R): Either[Throwable, R] =  {
    implicit val tx = db.beginTx()
    try {
      val result = f(db)
      tx.success()
      Right(result)
    }  catch {
      case ex =>
        tx.failure()
        Left(ex)
    } finally {
      tx.finish()
    }
  }
}

trait DatabaseAccessor[T <: GraphDatabaseService] {
  self : DatabaseTransactionManager[T] =>
  def apply(db: T)(f: (T) => Unit) = {
    inTx(db){
      f
    }
  }
}

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
