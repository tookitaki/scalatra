package org.scalatra.json

import com.tt.json4s.JValue

case class JsonResult(value: JValue)

object JsonResult {
  def apply[T](v: T)(implicit T: T => JValue): JsonResult = JsonResult(v: JValue)
}
