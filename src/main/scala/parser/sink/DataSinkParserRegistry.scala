package parser.sink

import core.sink.DataSink
import parser.{Parser, ParserRegistry}

/**
 * Registry of available [[DataSink]] parsers.
 *
 * This object provides a centralized lookup for parsers that can
 * convert configuration (e.g., JSON) into [[DataSink]] instances.
 *
 * Parsers are registered in an internal map keyed by their `name`,
 * allowing dynamic resolution at runtime.
 *
 * Currently registered parsers:
 *   - [[BigQuerySinkParser]]
 *
 * Example usage:
 * {{{
 *   val parser = DataSinkParserRegistry.get("BigQuerySink")
 *   val sink = parser.parse(cursor)
 * }}}
 */
object DataSinkParserRegistry extends ParserRegistry[DataSink] {
  /**
   * Internal map of parser name to parser instance.
   */
  private val parsers: Map[String, Parser[DataSink]] = List(
    BigQuerySinkParser
  ).map(p => p.name -> p).toMap

  /**
   * Retrieves a [[DataSink]] parser by name.
   *
   * @param name the name of the data source parser
   * @return the corresponding [[Parser]]
   * @throws IllegalArgumentException if the parser name is not registered
   */
  def get(name: String): Parser[DataSink] =
    parsers.getOrElse(
      name,
      throw new IllegalArgumentException(s"Unknown data source: $name")
    )
}