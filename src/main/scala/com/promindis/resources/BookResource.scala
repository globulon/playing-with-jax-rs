package com.promindis.resources

import javax.ws.rs.core.MediaType._
import javax.ws.rs._
import core.{UriInfo, Context}


@Path("/books")
@Produces(Array(APPLICATION_XML, APPLICATION_JSON))
@Consumes(Array(APPLICATION_XML, APPLICATION_JSON))
class BookResource {

  @Context
  var uriInfo: UriInfo = _

  @GET
  def listOfBooks() = Array(Book("123", "Scala in depth"), Book("4,5,6","Scala in Action"))


}