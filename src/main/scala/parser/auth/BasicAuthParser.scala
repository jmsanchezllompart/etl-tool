package parser.auth

import core.auth.{Auth, BasicAuth}
import io.circe.ACursor
import parser.Parser

object BasicAuthParser extends Parser[Auth] {
  override def name: String = "BasicAuth"

  override def parse(cursor: ACursor): Auth = {
    val user = cursor.get[String]("User") match {
      case Right(user) => user
      case Left(_) => throw new Exception()
    }

    val password = cursor.get[String]("Password") match {
      case Right(password) => password
      case Left(_) => throw new Exception()
    }

    BasicAuth(
      user = user,
      password = password
    )
  }
}
