package parser.auth

import core.auth.Auth
import parser.{Parser, ParserRegistry}

/**
 * Registry of available [[Auth]] parsers.
 *
 * This object provides a centralized lookup for parsers that can
 * convert configuration (e.g., JSON) into [[Auth]] instances.
 *
 * Parsers are registered in an internal map keyed by their `name`,
 * allowing dynamic resolution at runtime.
 *
 * Currently registered parsers:
 * - [[BasicAuthParser]]
 * - [[ServiceAccountAuthParser]]
 *
 * Example usage:
 * {{{
 *   val parser = AuthParserRegistry.get("BasicAuth")
 *   val auth = parser.parse(cursor)
 * }}}
 */
object AuthParserRegistry extends ParserRegistry[Auth] {
  /**
   * Internal map of parser name to parser instance.
   */
  private val parsers: Map[String, Parser[Auth]] = List(
    BasicAuthParser,
    ServiceAccountAuthParser
  ).map(p => p.name -> p).toMap

  /**
   * Retrieves a [[Auth]] parser by name.
   *
   * @param name the name of the data source parser
   * @return the corresponding [[Parser]]
   * @throws IllegalArgumentException if the parser name is not registered
   */
  def get(name: String): Parser[Auth] =
    parsers.getOrElse(
      name,
      throw new IllegalArgumentException(s"Unknown auth method: $name")
    )
}
