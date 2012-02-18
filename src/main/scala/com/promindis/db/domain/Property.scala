package com.promindis.db.domain

/**
 * Date: 18/02/12
 * Time: 13:12
 */

sealed  trait Property

object Property {
  implicit def toString(prop: Property): String = prop.toString
}

object UserProperties {
  sealed trait UserProperty extends Property
  case object USER_NAME extends  Property

}
