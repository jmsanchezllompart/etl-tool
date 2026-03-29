package parser.transformation

import core.transformation.Transformation
import parser.{Parser, ParserRegistry}

object TransformationParserRegistry extends ParserRegistry[Transformation] {

  private val parsers: Map[String, Parser[Transformation]] = List(
    RenameColumnParser
  ).map(p => p.name -> p).toMap

  def get(name: String): Parser[Transformation] =
    parsers.getOrElse(
      name,
      throw new IllegalArgumentException(s"Unknown transformation: $name")
    )
}