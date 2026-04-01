package parser.auth

import core.auth.{Auth, ServiceAccountAuth}
import io.circe.ACursor
import parser.Parser

/**
 * Parser implementation for constructing a [[ServiceAccountAuth]] from a JSON configuration.
 *
 * This parser reads required fields from a Circe [[io.circe.ACursor]] and converts them
 * into a fully configured [[ServiceAccountAuth]] instance.
 *
 * Expected JSON structure:
 * {{{
 * {
 *   "ServiceAccountFile": "/path/to/file.json"
 * }
 * }}}
 *
 * Fields:
 *  - ServiceAccountFile (String): The path to the Service Account JSON file
 *
 * @throws IllegalArgumentException if any required field is missing or invalid
 *                                  or if authentication parsing fails
 */
object ServiceAccountAuthParser extends Parser[Auth] {
  /** Identifier for this parser, used in parser registries. */
  override def name: String = "ServiceAccountAuth"

  /**
   * Parses a [[ServiceAccountAuth]] from the given JSON cursor.
   *
   * @param cursor The Circe [[io.circe.ACursor]] pointing to the JSON object
   *               representing a [[ServiceAccountAuth]] configuration.
   * @return A fully constructed [[ServiceAccountAuth]] instance.
   *
   * @throws IllegalArgumentException if:
   *                                  - Any required field is missing
   *                                  - Any field has an invalid type
   *                                  - The Auth sub-field cannot be parsed
   */
  override def parse(cursor: ACursor): Auth = {
    val serviceAccountFile = cursor.get[String]("ServiceAccountFile") match {
      case Right(serviceAccountFile) => serviceAccountFile
      case Left(error) => throw new IllegalArgumentException(
        s"[ServiceAccountAuthParser] Missing or invalid 'ServiceAccountFile' field: ${error.getMessage}"
      )
    }

    ServiceAccountAuth(
      serviceAccountFile = serviceAccountFile
    )
  }
}
