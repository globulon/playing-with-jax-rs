package com.promindis.resources

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}


@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
case class Book(title: String)