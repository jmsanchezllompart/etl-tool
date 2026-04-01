package parser.transformation

import core.transformation.Transformation
import parser.{Parser, ParserRegistry}

/**
 * Registry of available [[Transformation]] parsers.
 *
 * This object provides a centralized lookup for parsers that can
 * convert configuration (e.g., JSON) into [[Transformation]] instances.
 *
 * Parsers are registered in an internal map keyed by their `name`,
 * allowing dynamic resolution at runtime.
 *
 * Currently registered parsers:
 *   - [[RenameColumnParser]]
 *
 * Example usage:
 * {{{
 *   val parser = TransformationParserRegistry.get("RenameColumn")
 *   val transformation = parser.parse(cursor)
 * }}}
 */
object TransformationParserRegistry extends ParserRegistry[Transformation] {
  /**
   * Internal map of parser name to parser instance.
   */
  private val parsers: Map[String, Parser[Transformation]] = List(
    RenameColumnParser
  ).map(p => p.name -> p).toMap

  /**
   * Retrieves a [[Transformation]] parser by name.
   *
   * @param name the name of the transformation parser
   * @return the corresponding [[Parser]]
   * @throws IllegalArgumentException if the parser name is not registered
   */
  def get(name: String): Parser[Transformation] =
    parsers.getOrElse(
      name,
      throw new IllegalArgumentException(s"Unknown transformation: $name")
    )
}