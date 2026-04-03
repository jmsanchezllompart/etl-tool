package parser.appendmode

import core.sink.AppendMode
import parser.{Parser, ParserRegistry}

object AppendModeParserRegistry extends ParserRegistry[AppendMode] {
  private val parsers: Map[String, Parser[AppendMode]] = List(
    AppendModeParser,
    OverwriteModeParser,
    MergeModeParser
  ).map(p => p.name -> p).toMap

  override def get(name: String): Parser[AppendMode] =
    parsers.getOrElse(
      name,
      throw new IllegalArgumentException(s"Unknown append mode: $name")
    )
}
