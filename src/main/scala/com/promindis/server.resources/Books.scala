package com.promindis.server.resources

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter
import javax.xml.bind.annotation._
import Adapters.BookListToBookContainer

/**
 * Date: 17/02/12
 * Time: 15:49
 */
@XmlRootElement(name = "books")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = Array("items"))
final case class
Books(list: List[Book]) {

  def this() = this(List())

  @XmlElement
  @XmlJavaTypeAdapter(classOf[BookListToBookContainer])
  def getItems = list
}

@XmlRootElement(name = "book")
@XmlAccessorType(XmlAccessType.FIELD)
case class Book(
                 @XmlAttribute(required = true)isbn: String,
                 @XmlAttribute(required = true)title: String
                 ) {
  def this() = this (null, null)
}
