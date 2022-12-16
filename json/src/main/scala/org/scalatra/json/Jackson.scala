package org.scalatra
package json

import java.io.Writer

import com.fasterxml.jackson.databind.DeserializationFeature
import com.tookitaki.json4s._
import org.scalatra.util.RicherString._

trait JacksonJsonSupport extends JsonSupport[JValue] with JacksonJsonOutput with JValueResult {

  override def initialize(config: ConfigT): Unit = {
    super.initialize(config)
    mapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, jsonFormats.wantsBigDecimal)
  }

  protected def readJsonFromBody(bd: String): JValue = {
    if (bd.nonBlank) mapper.readValue(bd, classOf[JValue])
    else JNothing
  }
}

trait JacksonJsonOutput extends JsonOutput[JValue] with jackson.JsonMethods {
  protected def writeJson(json: JValue, writer: Writer): Unit = {
    if (json != JNothing) mapper.writeValue(writer, json)
  }
}
