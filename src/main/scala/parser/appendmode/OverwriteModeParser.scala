package parser.appendmode

import core.sink.{AppendMode, Overwrite}
import io.circe.ACursor
import parser.Parser

/**
 * Parser implementation for constructing a [[Overwrite]] from a JSON configuration.
 *
 * This parser reads required fields from a Circe [[io.circe.ACursor]] and converts them
 * into a fully configured [[Overwrite]] instance.
 */
object OverwriteModeParser extends Parser[AppendMode] {
  /** Identifier for this parser, used in parser registries. */
  override def name: String = "Overwrite"

  /**
   * Parses a [[Overwrite]] from the given JSON cursor.
   *
   * @param cursor The Circe [[io.circe.ACursor]] pointing to the JSON object
   *               representing an Overwrite append mode configuration.
   * @return A fully constructed [[Overwrite]] instance.
   */
  override def parse(cursor: ACursor): AppendMode = {
    Overwrite()
  }
}
