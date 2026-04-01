package parser.source

import core.source.DataSource
import parser.{Parser, ParserRegistry}

object DataSourceParserRegistry extends ParserRegistry[DataSource] {
  private val parsers: Map[String, Parser[DataSource]] = List(
    PostgresSqlParser,
    MySqlSourceParser,
    SqlServerSourceParser
  ).map(p => p.name -> p).toMap

  def get(name: String): Parser[DataSource] =
    parsers.getOrElse(
      name,
      throw new IllegalArgumentException(s"Unknown data source: $name")
    )
}
