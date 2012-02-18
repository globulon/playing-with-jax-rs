package com.promindis.db.adatpers

import org.neo4j.kernel.EmbeddedGraphDatabase
import DatabaseTools._

/**
 * Date: 17/02/12
 * Time: 21:14
 */



object DatabaseFactory {
  lazy val PATH = "neo-db"

  implicit object EmbeddedDatabaseLifeCycleManager extends DatabaseLifeCycleManager[EmbeddedGraphDatabase]
  implicit object EmbeddedDatabaseAccessor
    extends DatabaseAccessor[EmbeddedGraphDatabase]
    with DatabaseTransactionManager[EmbeddedGraphDatabase]


  def createEmbeddedDb() = Database(new EmbeddedGraphDatabase(PATH), PATH).setup()


}
