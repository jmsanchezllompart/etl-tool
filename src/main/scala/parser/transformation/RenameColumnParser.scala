package parser.transformation

import core.transformation.{RenameColumn, Transformation}
import io.circe.ACursor
import parser.Parser

/**
 * Parser implementation for constructing a [[RenameColumn]] from a JSON configuration.
 *
 * This parser reads required fields from a Circe [[io.circe.ACursor]] and converts them
 * into a fully configured [[RenameColumn]] instance.
 *
 * Expected JSON structure:
 * {{{
 * {
 *   "OldColumnName": "old_name",
 *   "NewColumnName": "new_name"
 * }
 * }}}
 *
 * Fields:
 *  - OldColumnName (String): The obsolete column name
 *  - NewColumnName (String): The new column name
 *
 * @throws IllegalArgumentException if any required field is missing or invalid
 */
object RenameColumnParser extends Parser[Transformation] {
  /** Identifier for this parser, used in parser registries. */
  override val name: String = "RenameColumn"

  /**
   * Parses a [[RenameColumn]] from the given JSON cursor.
   *
   * @param cursor The Circe [[io.circe.ACursor]] pointing to the JSON object
   *               representing a RenameColumn transformation configuration.
   * @return A fully constructed [[RenameColumn]] instance.
   *
   * @throws IllegalArgumentException if:
   *                                  - Any required field is missing
   *                                  - Any field has an invalid type
   */
  override def parse(cursor: ACursor): Transformation = {
    val oldName = cursor.get[String]("OldColumnName") match {
      case Right(oldName) => oldName
      case Left(error) => throw new IllegalArgumentException(
        s"[RenameColumnParser] Missing or invalid 'OldColumnName' field: ${error.getMessage}"
      )
    }

    val newName = cursor.get[String]("NewColumnName") match {
      case Right(newName) => newName
      case Left(error) => throw new IllegalArgumentException(
        s"[RenameColumnParser] Missing or invalid 'NewColumnName' field: ${error.getMessage}"
      )
    }

    RenameColumn(
      oldName = oldName,
      newName = newName
    )
  }
}