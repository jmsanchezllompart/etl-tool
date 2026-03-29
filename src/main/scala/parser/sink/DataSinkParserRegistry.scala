package parser.sink

import core.sink.DataSink
import parser.{Parser, ParserRegistry}

object DataSinkParserRegistry extends ParserRegistry[DataSink] {
  private val parsers: Map[String, Parser[DataSink]] = List(
    BigQuerySinkParser
  ).map(p => p.name -> p).toMap

  def get(name: String): Parser[DataSink] =
    parsers.getOrElse(
      name,
      throw new IllegalArgumentException(s"Unknown data source: $name")
    )
}