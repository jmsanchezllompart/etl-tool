package parser.sink

import core.auth.Auth
import core.sink.{AppendMode, BigQuerySink, DataSink}
import io.circe.ACursor
import parser.Parser
import parser.appendmode.AppendModeParserRegistry
import parser.auth.AuthParserRegistry
import parser.helpers.Helpers.parseSubField

/**
 * Parser implementation for constructing a [[BigQuerySink]] from a JSON configuration.
 *
 * This parser reads required fields from a Circe [[io.circe.ACursor]] and converts them
 * into a fully configured [[BigQuerySink]] instance.
 *
 * Expected JSON structure:
 * {{{
 * {
 *   "Project": "test-project",
 *   "Dataset": "test-dataset",
 *   "Table": "test-table",
 *   "Auth": { ... },
 *   "AppendMode": { ... }
 * }
 * }}}
 *
 * Fields:
 *  - Project (String): BigQuery project id
 *  - Dataset (String): BigQuery dataset id
 *  - Table (String): BigQuery table name
 *  - Auth (Object): Authentication configuration (delegated to [[AuthParserRegistry]])
 *  - AppendMode (Object): Append Mode configuration
 *
 * @throws IllegalArgumentException if any required field is missing or invalid
 *                                  or if authentication parsing fails
 */
object BigQuerySinkParser extends Parser[DataSink] {
  /** Identifier for this parser, used in parser registries. */
  override def name: String = "BigQuerySink"

  /**
   * Parses a [[BigQuerySink]] from the given JSON cursor.
   *
   * @param cursor The Circe [[io.circe.ACursor]] pointing to the JSON object
   *               representing a BigQuery sink configuration.
   * @return A fully constructed [[BigQuerySink]] instance.
   *
   * @throws IllegalArgumentException if:
   *                                  - Any required field is missing
   *                                  - Any field has an invalid type
   *                                  - The Auth sub-field cannot be parsed
   */
  override def parse(cursor: ACursor): DataSink = {
    val project = cursor.get[String]("Project") match {
      case Right(project) => project
      case Left(error) => throw new IllegalArgumentException(
        s"[BigQuerySinkParser] Missing or invalid 'Project' field: ${error.getMessage}"
      )
    }

    val dataset = cursor.get[String]("Dataset") match {
      case Right(dataset) => dataset
      case Left(error) => throw new IllegalArgumentException(
        s"[BigQuerySinkParser] Missing or invalid 'Dataset' field: ${error.getMessage}"
      )
    }

    val table = cursor.get[String]("Table") match {
      case Right(table) => table
      case Left(error) => throw new IllegalArgumentException(
        s"[BigQuerySinkParser] Missing or invalid 'Table' field: ${error.getMessage}"
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
            s"[BigQuerySinkParser] Failed to parse 'Auth' field: ${e.getMessage}",
            e
          )
      }

    val appendMode =
      try {
        parseSubField[AppendMode](
          cursor = cursor,
          parserRegistry = AppendModeParserRegistry,
          fieldKey = "AppendMode"
        )
      } catch {
        case e: Exception =>
          throw new IllegalArgumentException(
            s"[BigQuerySinkParser] Failed to parse 'AppendMode' field: ${e.getMessage}",
            e
          )
      }

    BigQuerySink(
      project = project,
      dataset = dataset,
      table = table,
      auth = auth,
      appendMode = appendMode
    )
  }
}
