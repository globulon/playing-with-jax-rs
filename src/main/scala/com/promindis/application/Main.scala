package com.promindis.application

import com.promindis.db.adatpers.DatabaseFactory._
import com.promindis.db.scenarii._

/**
 * Date: 17/02/12
 * Time: 21:49
 */


object Main extends App {

  val db = createEmbeddedDb()

  db {SayHello}

  db {PlayWithUsers}

  db {MatrixTraversal}

  db {MatrixTraversalNewGen}

  db {PetStore}

  db.shutdown()
}
