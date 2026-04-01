package parser.helpers

import io.circe.ACursor
import parser.ParserRegistry

/**
 * Utility helper methods for parsing structured data using `Parser` and `ParserRegistry`.
 *
 * This object provides reusable functions for extracting and parsing
 * nested fields (single or multiple) from a JSON/YAML structure using
 * Circe cursors and a registry of parsers.
 */
object Helpers {
  /**
   * Parses a single nested field using a parser resolved from a registry.
   *
   * The expected structure is:
   * {{{
   * fieldKey:
   *   parserName:
   *     ... parser-specific fields ...
   * }}}
   *
   * Exactly one parser type must be specified under `fieldKey`.
   *
   * @param cursor the root [[ACursor]]
   * @param parserRegistry the [[ParserRegistry]] used to resolve the parser
   * @param fieldKey the field containing the nested parser definition
   * @tparam T the type produced by the parser
   * @return the parsed object of type `T`
   *
   * @throws IllegalArgumentException if:
   *   - the field is missing or malformed
   *   - multiple parser types are defined
   *   - no parser type is defined
   */
  def parseSubField[T](cursor: ACursor, parserRegistry: ParserRegistry[T], fieldKey: String): T = {
    val subCursor = cursor.downField(fieldKey)
    val tTypes = subCursor.keys.getOrElse(throw new IllegalArgumentException("Invalid YAML format"))

    val tType = if (tTypes.size == 1) {
      tTypes.head
    } else if (tTypes.size > 1) {
      throw new IllegalArgumentException(s"Multiple $fieldKey found, please choose only one")
    } else {
      throw new IllegalArgumentException(s"At least one $fieldKey must be provided")
    }

    val parser = parserRegistry.get(tType)
    parser.parse(subCursor.downField(tType))
  }

  /**
   * Parses a list of nested fields using parsers resolved from a registry.
   *
   * The expected structure is:
   * {{{
   * fieldKey:
   *   - parserName1:
   *       ... parser-specific fields ...
   *   - parserName2:
   *       ... parser-specific fields ...
   * }}}
   *
   * Each element must define exactly one parser type.
   *
   * @param cursor the root [[ACursor]]
   * @param parserRegistry the [[ParserRegistry]] used to resolve parsers
   * @param fieldKey the field containing the list of parser definitions
   * @tparam T the type produced by the parsers
   * @return a list of parsed objects of type `T`
   *
   * @throws NoSuchElementException if the field is missing
   * @throws IllegalArgumentException if any element is malformed
   */
  def parseSubFieldsList[T](cursor: ACursor, parserRegistry: ParserRegistry[T], fieldKey: String): List[T] = {
    val values = cursor.downField(fieldKey).values.get

    values.map { value =>
      val subCursor = value.hcursor
      val key = subCursor.keys.get.head
      val parser = parserRegistry.get(key)
      parser.parse(subCursor.downField(key))
    }.toList
  }
}

