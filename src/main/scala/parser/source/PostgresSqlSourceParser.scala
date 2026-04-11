package parser.source

import core.auth.Auth
import core.source.{DataSource, PostgresSqlSource}
import io.circe.ACursor
import parser.Parser
import parser.auth.AuthParserRegistry
import parser.helpers.Helpers.parseSubField

/**
 * Parser implementation for constructing a [[PostgresSqlSource]] from a JSON configuration.
 *
 * This parser reads required fields from a Circe [[io.circe.ACursor]] and converts them
 * into a fully configured [[PostgresSqlSource]] instance.
 *
 * Expected JSON structure:
 * {{{
 * {
 *   "Host": "localhost",
 *   "Port": 5432,
 *   "Database": "my_db",
 *   "Auth": { ... },
 *   "RawQuery": "SELECT * FROM table"
 * }
 * }}}
 *
 * Fields:
 *  - Host (String): PostgresSQL server hostname or IP
 *  - Port (Int): PostgresSQL server port
 *  - Database (String): Target database name
 *  - Auth (Object): Authentication configuration (delegated to [[AuthParserRegistry]])
 *  - RawQuery (String): SQL query to execute
 *
 * @throws IllegalArgumentException if any required field is missing or invalid
 *                                  or if authentication parsing fails
 */
object PostgresSqlSourceParser extends Parser[DataSource] {
  /** Identifier for this parser, used in parser registries. */
  override def name: String = "PostgresSqlSource"

  /**
   * Parses a [[PostgresSqlSource]] from the given JSON cursor.
   *
   * @param cursor The Circe [[io.circe.ACursor]] pointing to the JSON object
   *               representing a PostgresSQL data source configuration.
   * @return A fully constructed [[PostgresSqlSource]] instance.
   *
   * @throws IllegalArgumentException if:
   *                                  - Any required field is missing
   *                                  - Any field has an invalid type
   *                                  - The Auth sub-field cannot be parsed
   */
  override def parse(cursor: ACursor): DataSource = {
    val host = cursor.get[String]("Host") match {
      case Right(host) => host
      case Left(error) => throw new IllegalArgumentException(
        s"[PostgresSqlSourceParser] Missing or invalid 'Host' field: ${error.getMessage}"
      )
    }

    val port = cursor.get[Int]("Port") match {
      case Right(port) => port.toString
      case Left(error) => throw new IllegalArgumentException(
        s"[PostgresSqlSourceParser] Missing or invalid 'Port' field: ${error.getMessage}"
      )
    }

    val database = cursor.get[String]("Database") match {
      case Right(database) => database
      case Left(error) => throw new IllegalArgumentException(
        s"[PostgresSqlSourceParser] Missing or invalid 'Database' field: ${error.getMessage}"
      )
    }

    /**
     * Parse authentication configuration using the registered Auth parsers.
     * Wraps any underlying exception with additional context.
     */
    val auth =
      try {
        parseSubField[Auth](
          cursor = cursor,
          parserRegistry = AuthParserRegistry,
          fieldKey = "Auth"
        )
      } catch {
        case e: Exception =>
          throw new IllegalArgumentException(
            s"[PostgresSqlSourceParser] Failed to parse 'Auth' field: ${e.getMessage}",
            e
          )
      }

    val query = cursor.get[String]("RawQuery") match {
      case Right(query) => query
      case Left(error) => throw new IllegalArgumentException(
        s"[PostgresSqlSourceParser] Missing or invalid 'RawQuery' field: ${error.getMessage}"
      )
    }

    PostgresSqlSource(
      host = host,
      port = port,
      database = database,
      auth = auth,
      query = query
    )
  }
}
