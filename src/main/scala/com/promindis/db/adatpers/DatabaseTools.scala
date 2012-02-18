package com.promindis.db.adatpers

import java.lang.Runtime._
import org.neo4j.kernel.impl.util.FileUtils._
import java.nio.file.Paths._
import org.neo4j.graphdb.index.Index
import org.neo4j.graphdb.{Node, GraphDatabaseService}

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
    def inTx[R](database: Database[T])(f: (Database[T]) => R): Either[Throwable, R] = {
      implicit val tx = database.instance.beginTx()
      try {
        val result = f(database)
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

  trait DatabaseIndexer[T <: GraphDatabaseService] {

    def newIndexedNode[K](instance: T, key: K, name: String)(implicit index: Index[Node], f: K => String) = {
      val node = instance.createNode()
      node.setProperty(key, name)
      index.add(node, key, name)
      node
    }

    def foundNode[K](key: K, searchedCriteria: Any)(implicit index: Index[Node], f: K => String): Node = {
      index.get(key, searchedCriteria).getSingle
    }

  }

  trait DatabaseAccessor[T <: GraphDatabaseService] extends DatabaseIndexer[T]{
    self: DatabaseTransactionManager[T]  =>
    def apply(db: Database[T])(f: (Database[T]) => Unit) = {
      inTx(db) {
        f
      }
    }
  }

}
