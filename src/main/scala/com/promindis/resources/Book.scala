package com.promindis.resources

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement, XmlAttribute}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class Book(
  @XmlAttribute(required = true)isbn: String,
  @XmlAttribute(required = true)title: String
) {
  def this() = this (null, null)
}