package parser.appendmode

import core.sink.{Append, AppendMode}
import io.circe.ACursor
import parser.Parser

/**
 * Parser implementation for constructing a [[Append]] from a JSON configuration.
 *
 * This parser reads required fields from a Circe [[io.circe.ACursor]] and converts them
 * into a fully configured [[Append]] instance.
 */
object AppendModeParser extends Parser[AppendMode] {
  /** Identifier for this parser, used in parser registries. */
  override def name: String = "Append"

  /**
   * Parses a [[Append]] from the given JSON cursor.
   *
   * @param cursor The Circe [[io.circe.ACursor]] pointing to the JSON object
   *               representing an Append mode configuration.
   * @return A fully constructed [[Append]] instance.
   */
  override def parse(cursor: ACursor): AppendMode = {
    Append()
  }
}
