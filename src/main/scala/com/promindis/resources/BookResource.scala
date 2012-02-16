package com.promindis.resources

import javax.ws.rs.core.MediaType._
import javax.ws.rs._


@Path("books")
@Produces(Array(APPLICATION_XML, APPLICATION_JSON))
@Consumes(Array(APPLICATION_XML, APPLICATION_JSON))
class BookResource {

  @GET
  def listOfBooks() = List(Book("Scala in depth"), Book("Scala in Action"))

}