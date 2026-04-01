package parser.auth

import core.auth.{Auth, BasicAuth}
import io.circe.ACursor
import parser.Parser

/**
 * Parser implementation for constructing a [[BasicAuth]] from a JSON configuration.
 *
 * This parser reads required fields from a Circe [[io.circe.ACursor]] and converts them
 * into a fully configured [[BasicAuth]] instance.
 *
 * Expected JSON structure:
 * {{{
 * {
 *   "User": "user",
 *   "Password": "password"
 * }
 * }}}
 *
 * Fields:
 *  - User (String): The user to auth
 *  - Password (String): The password to auth
 *
 * @throws IllegalArgumentException if any required field is missing or invalid
 *                                  or if authentication parsing fails
 */
object BasicAuthParser extends Parser[Auth] {
  /** Identifier for this parser, used in parser registries. */
  override def name: String = "BasicAuth"

  /**
   * Parses a [[BasicAuth]] from the given JSON cursor.
   *
   * @param cursor The Circe [[io.circe.ACursor]] pointing to the JSON object
   *               representing a [[BasicAuth]] configuration.
   * @return A fully constructed [[BasicAuth]] instance.
   *
   * @throws IllegalArgumentException if:
   *                                  - Any required field is missing
   *                                  - Any field has an invalid type
   *                                  - The Auth sub-field cannot be parsed
   */
  override def parse(cursor: ACursor): Auth = {
    val user = cursor.get[String]("User") match {
      case Right(user) => user
      case Left(error) => throw new IllegalArgumentException(
        s"[BasicAuthParser] Missing or invalid 'User' field: ${error.getMessage}"
      )
    }

    val password = cursor.get[String]("Password") match {
      case Right(password) => password
      case Left(error) => throw new IllegalArgumentException(
        s"[BasicAuthParser] Missing or invalid 'Password' field: ${error.getMessage}"
      )
    }

    BasicAuth(
      user = user,
      password = password
    )
  }
}
