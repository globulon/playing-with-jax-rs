package com.promindis.db.domain

/**
 * Date: 18/02/12
 * Time: 13:12
 */

sealed  trait Property

object Property {
  implicit def toString(prop: Property): String = prop.toString
}

object SoftwareProperties {
  sealed trait SoftwareProperty extends Property
  case object VERSION extends SoftwareProperty
  case object LANGUAGE extends SoftwareProperty
}

object UserProperties {
  sealed trait UserProperty extends Property
  case object USER_NAME extends  Property
  case object USER_LAST_NAME extends  Property
  case object USER_OCCUPATION extends Property
  case object USER_RANK extends Property

}
