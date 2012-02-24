package com.promindis.server.resources

import javax.xml.bind.annotation.{XmlRootElement, XmlAccessorType, XmlAccessType}
import XmlAccessType._
import JAXBAdapters._

/**
 * Date: 23/02/12
 * Time: 18:24
 */
@XmlRootElement(name = "person")
@XmlAccessorType(FIELD)
final case class Person
(
  @xmlElement(required = true) fullname: String,
  @xmlTypeAdapter(classOf[StringOptionAdapter]) username: Option[String],
  age: Int
  )   {
  // only needed and accessed by JAXB
  private def this() = this("", None, 0)
}
