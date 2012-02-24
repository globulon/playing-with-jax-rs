package com.promindis.application

import com.sun.jersey.api.json._
import com.promindis.server.resources.Person
import com.promindis.support.Tools._
import java.io.{StringReader, ByteArrayInputStream, OutputStream}

/**
 * Date: 24/02/12
 * Time: 10:54
 */

object JSONGeneration {
  def config = JSONConfiguration.natural().rootUnwrapping(false).build()

  implicit def extended(p: Person)(implicit context: JSONJAXBContext) = new {
    def marshall(o: OutputStream) {
      context.createJSONMarshaller().marshallToJSON(p, o)
    }
  }

  implicit def extended(j: String)(implicit context: JSONJAXBContext) = new {
    def unmarshalled =
      context.createJSONUnmarshaller().unmarshalFromJSON(new StringReader(j), classOf[Person])
  }


  def main(args: Array[String]) {
    implicit val context = new JSONJAXBContext(config, classOf[Person])

    trace {
      Person("Thumper", Some("ThumpThump"), 10).marshall
    }

    println("{\"person\":{\"fullname\":\"Thumper\",\"username\":\"ThumpThump\",\"age\":10}}".unmarshalled)

  }
}
