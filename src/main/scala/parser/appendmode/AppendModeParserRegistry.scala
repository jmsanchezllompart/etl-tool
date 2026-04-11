package parser.appendmode

import core.sink.AppendMode
import parser.{Parser, ParserRegistry}

/**
 * Registry of available [[AppendMode]] parsers.
 *
 * This object provides a centralized lookup for parsers that can
 * convert configuration (e.g., JSON) into [[AppendMode]] instances.
 *
 * Parsers are registered in an internal map keyed by their `name`,
 * allowing dynamic resolution at runtime.
 *
 * Currently registered parsers:
 *   - [[AppendModeParser]]
 *   - [[OverwriteModeParser]]
 *   - [[MergeModeParser]]
 *
 * Example usage:
 * {{{
 *   val parser = AppendModeParserRegistry.get("Append")
 *   val appendMode = parser.parse(cursor)
 * }}}
 */
object AppendModeParserRegistry extends ParserRegistry[AppendMode] {
  /**
   * Internal map of parser name to parser instance.
   */
  private val parsers: Map[String, Parser[AppendMode]] = List(
    AppendModeParser,
    OverwriteModeParser,
    MergeModeParser
  ).map(p => p.name -> p).toMap

  /**
   * Retrieves a [[AppendMode]] parser by name.
   *
   * @param name the name of the transformation parser
   * @return the corresponding [[Parser]]
   * @throws IllegalArgumentException if the parser name is not registered
   */
  override def get(name: String): Parser[AppendMode] =
    parsers.getOrElse(
      name,
      throw new IllegalArgumentException(s"Unknown append mode: $name")
    )
}
