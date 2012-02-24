package com.promindis.application

import com.promindis.server.resources.Person
import javax.xml.bind.{SchemaOutputResolver, JAXBContext}
import javax.xml.transform.stream.StreamResult
import java.io.StringReader
/**
 * Date: 23/02/12
 * Time: 19:38
 */

object SchemaGenerator {
  def main(args: Array[String]) {
    val context = JAXBContext.newInstance(classOf[Person])
    context.generateSchema(new SchemaOutputResolver {
      override def createOutput(namespaceUri: String, suggestedFileName: String) = {
        val res = new StreamResult(System.out)
        res.setSystemId(suggestedFileName)
        res
      }
    })

    context.createMarshaller.marshal(
      Person("Martin Krasser", Some("krasserm"), 30),
        System.out)
    val personXml1 = "<person><fullname>Martin Krasser</fullname><age>30</age></person>"
    val personXml2 = "<person><fullname>Martin Krasser</fullname><username>mrt1nz</username><age>30</age></person>"
    println(System.getProperty("java.endorsed.dirs"))

    println(context.createUnmarshaller().unmarshal(new StringReader(personXml1)))
    println(context.createUnmarshaller().unmarshal(new StringReader(personXml2)))
  }
}
