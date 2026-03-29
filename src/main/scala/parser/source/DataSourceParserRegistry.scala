package parser.source

object DataSourceParserRegistry {
  private val parsers: Map[String, DataSourceParser] = List(
    PostgresSqlParser
  ).map(p => p.name -> p).toMap

  def get(name: String): DataSourceParser =
    parsers.getOrElse(
      name,
      throw new IllegalArgumentException(s"Unknown data source: $name")
    )
}
