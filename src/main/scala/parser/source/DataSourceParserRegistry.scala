package parser.source

import core.source.DataSource
import parser.{Parser, ParserRegistry}

/**
 * Registry of available [[DataSource]] parsers.
 *
 * This object provides a centralized lookup for parsers that can
 * convert configuration (e.g., JSON) into [[DataSource]] instances.
 *
 * Parsers are registered in an internal map keyed by their `name`,
 * allowing dynamic resolution at runtime.
 *
 * Currently registered parsers:
 *   - [[PostgresSqlSourceParser]]
 *   - [[MySqlSourceParser]]
 *   - [[SqlServerSourceParser]]
 *
 * Example usage:
 * {{{
 *   val parser = DataSourceParserRegistry.get("SqlServerSource")
 *   val source = parser.parse(cursor)
 * }}}
 */
object DataSourceParserRegistry extends ParserRegistry[DataSource] {
  /**
   * Internal map of parser name to parser instance.
   */
  private val parsers: Map[String, Parser[DataSource]] = List(
    PostgresSqlSourceParser,
    MySqlSourceParser,
    SqlServerSourceParser
  ).map(p => p.name -> p).toMap

  /**
   * Retrieves a [[DataSource]] parser by name.
   *
   * @param name the name of the data source parser
   * @return the corresponding [[Parser]]
   * @throws IllegalArgumentException if the parser name is not registered
   */
  def get(name: String): Parser[DataSource] =
    parsers.getOrElse(
      name,
      throw new IllegalArgumentException(s"Unknown data source: $name")
    )
}
