package parser.auth

object AuthParserRegistry {
  private val parsers: Map[String, AuthParser] = List(
    BasicAuthParser,
    ServiceAccountAuthParser
  ).map(p => p.name -> p).toMap

  def get(name: String): AuthParser =
    parsers.getOrElse(
      name,
      throw new IllegalArgumentException(s"Unknown auth method: $name")
    )

  def list(): List[String] =
    parsers.keys.toList
}
