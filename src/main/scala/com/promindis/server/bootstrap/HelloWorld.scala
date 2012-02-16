package com.promindis.server.bootstrap

import java.io.{OutputStreamWriter, BufferedWriter}
import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}

class HelloWorld extends HttpServlet{
  override def doGet(req: HttpServletRequest, resp: HttpServletResponse) {
       val writer = new BufferedWriter(new OutputStreamWriter(resp.getOutputStream))
    writer.write("Hello world")
    writer.flush()
    writer.close()

  }
}

