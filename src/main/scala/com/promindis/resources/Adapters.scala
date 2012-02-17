package com.promindis.resources

import javax.xml.bind.annotation.adapters.XmlAdapter
import scala.collection._
import java.util.{List => JList, ArrayList => AList}
import javax.xml.bind.annotation._

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

  @XmlRootElement
  @XmlAccessorType(XmlAccessType.PROPERTY)
  @XmlType(propOrder = Array("book"))
  case class BookContainer(items: JList[Book]) extends Container[Book] {
    def this() = this(new AList[Book]())

    @XmlElement
    def getBook = items
  }

  trait ListToContainer[T , A <: Container[T]] extends XmlAdapter[A, List[T]] {
  import JavaConverters._

    def embed(list: JList[T]): A

    def marshal(seq: List[T]) = if (!seq.isEmpty) embed(seq.asJava) else embed(new AList[T])

    def unmarshal(container: A) = container.items.asScala.toList
  }

}
