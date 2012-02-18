package com.promindis.db.bootstrap

import org.neo4j.graphdb.GraphDatabaseService
import java.lang.Runtime._
import org.neo4j.kernel.impl.util.FileUtils._
import java.nio.file.Paths._

/**
 * Date: 18/02/12
 * Time: 13:51
 */

object DatabaseTools {

  trait DatabaseLifeCycleManager[T <: GraphDatabaseService] {
    def setup(db: T) {
      getRuntime.addShutdownHook(new Thread() {
        override def run() {
          db.shutdown()
        }
      })
    }

    def shutdown(db: T)(path: String) {
      db.shutdown()
      deleteRecursively(get(path).toFile)
    }
  }

  trait DatabaseTransactionManager[T <: GraphDatabaseService] {
    def inTx[R](db: T)(f: (T) => R): Either[Throwable, R] = {
      implicit val tx = db.beginTx()
      try {
        val result = f(db)
        tx.success()
        Right(result)
      } catch {
        case ex =>
          tx.failure()
          Left(ex)
      } finally {
        tx.finish()
      }
    }
  }

  trait DatabaseAccessor[T <: GraphDatabaseService] {
    self: DatabaseTransactionManager[T] =>
    def apply(db: T)(f: (T) => Unit) = {
      inTx(db) {
        f
      }
    }
  }

}
