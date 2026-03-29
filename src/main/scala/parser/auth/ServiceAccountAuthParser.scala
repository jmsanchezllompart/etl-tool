package parser.auth

import core.auth.{Auth, ServiceAccountAuth}

import scala.jdk.CollectionConverters.MapHasAsScala

object ServiceAccountAuthParser extends AuthParser {
  override def name: String = "ServiceAccountAuth"

  override def parse(value: Any): Auth = {
    val map = value
      .asInstanceOf[java.util.Map[String, Object]]
      .asScala

    ServiceAccountAuth(
      serviceAccountFile = map("ServiceAccountAuth").toString
    )
  }
}
