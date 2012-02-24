package com.promindis.support

import java.io.{ByteArrayOutputStream, BufferedOutputStream, OutputStream}


/**
 * Date: 24/02/12
 * Time: 11:03
 */

object Tools {

  def trace[O >: OutputStream](f: => O => Unit) {
    val output = new ByteArrayOutputStream()
    val buffer = new BufferedOutputStream(output)
    try {
      f(buffer)
      buffer.flush()
      println(new String(output.toByteArray))
    } finally {
      output.close()
      buffer.close()
    }
  }
}
