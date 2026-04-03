package parser.appendmode

import core.sink.{AppendMode, Merge}
import io.circe.ACursor
import parser.Parser

/**
 * Parser implementation for constructing a [[Merge]] from a JSON configuration.
 *
 * This parser reads required fields from a Circe [[io.circe.ACursor]] and converts them
 * into a fully configured [[Merge]] instance.
 *
 * Expected JSON structure:
 * {{{
 * {
 *   "IdFields": [
 *     "user_id"
 *   ]
 * }
 * }}}
 *
 * Fields:
 *  - IdFields (List[String]): A list of the fields used as the primary key to perform the merge
 *
 * @throws IllegalArgumentException if any required field is missing or invalid
 */
object MergeModeParser extends Parser[AppendMode] {
  /** Identifier for this parser, used in parser registries. */
  override def name: String = "Merge"

  /**
   * Parses a [[Merge]] from the given JSON cursor.
   *
   * @param cursor The Circe [[io.circe.ACursor]] pointing to the JSON object
   *               representing a Merge append mode configuration.
   * @return A fully constructed [[Merge]] instance.
   *
   * @throws IllegalArgumentException if:
   *                                  - Any required field is missing
   *                                  - Any field has an invalid type
   */
  override def parse(cursor: ACursor): AppendMode = {
    val idFields = cursor.get[List[String]]("IdFields") match {
      case Right(project) => project
      case Left(error) => throw new IllegalArgumentException(
        s"[MergeModeParser] Missing or invalid 'IdFields' field: ${error.getMessage}"
      )
    }

    Merge(idFields)
  }
}
