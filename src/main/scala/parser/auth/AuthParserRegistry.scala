package parser.auth

import core.auth.Auth
import parser.{Parser, ParserRegistry}

object AuthParserRegistry extends ParserRegistry[Auth] {
  private val parsers: Map[String, Parser[Auth]] = List(
    BasicAuthParser,
    ServiceAccountAuthParser
  ).map(p => p.name -> p).toMap

  def get(name: String): Parser[Auth] =
    parsers.getOrElse(
      name,
      throw new IllegalArgumentException(s"Unknown auth method: $name")
    )
}
