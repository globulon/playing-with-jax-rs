package com.promindis.resources

import javax.xml.bind.annotation.adapters.XmlAdapter
import scala.collection._
import java.util.{List => JList, ArrayList => AList}
import javax.xml.bind.annotation.XmlRootElement._
import javax.xml.bind.annotation.{XmlElementRef, XmlAccessType, XmlAccessorType, XmlRootElement}
import java.util.Arrays.ArrayList

/**
 * Date: 17/02/12
 * Time: 16:02
 */

object Adapters {

  trait Container[T] {
    def items : JList[T]
  }

  final class BookListToBookContainer() extends ListToContainer[Book, BookContainer] {
    override  def embed(list: JList[Book]) = BookContainer(list)
  }

  @XmlRootElement(name = "items")
  @XmlAccessorType(XmlAccessType.FIELD)
  case class BookContainer(@XmlElementRef(name = "items") items: JList[Book]) extends Container[Book] {
    def this() = this(new AList[Book]())
  }

  trait ListToContainer[T , A <: Container[T]] extends XmlAdapter[A, List[T]] {

    import JavaConverters._

    def embed(list: JList[T]): A

    def marshal(seq: List[T]) = embed(seq.asJava)

    def unmarshal(container: A) = container.items.asScala.toList
  }

}
