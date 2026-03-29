package parser.auth

import core.auth.{Auth, ServiceAccountAuth}
import io.circe.ACursor
import parser.Parser

object ServiceAccountAuthParser extends Parser[Auth] {
  override def name: String = "ServiceAccountAuth"

  override def parse(cursor: ACursor): Auth = {
    val serviceAccountFile = cursor.get[String]("ServiceAccountFile") match {
      case Right(serviceAccountFile) => serviceAccountFile
      case Left(_) => throw new Exception()
    }

    ServiceAccountAuth(
      serviceAccountFile = serviceAccountFile
    )
  }
}
