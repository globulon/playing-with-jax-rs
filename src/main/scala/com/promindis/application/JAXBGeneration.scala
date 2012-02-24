package com.promindis.application

import com.promindis.server.resources.Person
import javax.xml.bind.{SchemaOutputResolver, JAXBContext}
import javax.xml.transform.stream.StreamResult
import java.io.{ByteArrayOutputStream, StringReader}

/**
 * Date: 23/02/12
 * Time: 19:38
 */

object JAXBGeneration {

  def generateSchema(implicit context: JAXBContext) {
    val output = new ByteArrayOutputStream()

    context.generateSchema(new SchemaOutputResolver {
      override def createOutput(namespaceUri: String, suggestedFileName: String) = {
        val res = new StreamResult(output)
        res.setSystemId(suggestedFileName)
        res
      }
    })

    println(new String(output.toByteArray))

  }

  def marshall(implicit context: JAXBContext) {
    context.createMarshaller.marshal(
      Person("Martin Krasser", Some("krasserm"), 30),
      System.out)
  }

  def unmarshall(implicit context: JAXBContext) {
    val personXml1 = "<person><fullname>Martin Krasser</fullname><age>30</age></person>"
    val personXml2 = "<person><fullname>Martin Krasser</fullname><username>mrt1nz</username><age>30</age></person>"

    println(context.createUnmarshaller().unmarshal(new StringReader(personXml1)))
    println(context.createUnmarshaller().unmarshal(new StringReader(personXml2)))
  }

  def main(args: Array[String]) {
    implicit val context = JAXBContext.newInstance(classOf[Person])
    generateSchema
    marshall
    unmarshall
  }
}
