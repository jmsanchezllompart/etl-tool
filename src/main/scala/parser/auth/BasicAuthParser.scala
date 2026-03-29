package parser.auth

import core.auth.{Auth, BasicAuth}

import scala.jdk.CollectionConverters.MapHasAsScala

object BasicAuthParser extends AuthParser {
  override def name: String = "BasicAuth"

  override def parse(value: Any): Auth = {
    val map = value
      .asInstanceOf[java.util.Map[String, Object]]
      .asScala

    BasicAuth(
      user = map("User").toString,
      password = map("Password").toString
    )
  }
}
