package parser.pipeline

import core.pipeline.DataPipelineMetadata
import io.circe.ACursor
import parser.Parser

/**
 * Parser implementation for constructing a [[DataPipelineMetadata]] from a JSON configuration.
 *
 * This parser reads required fields from a Circe [[io.circe.ACursor]] and converts them
 * into a fully configured [[DataPipelineMetadata]] instance.
 *
 * Expected JSON structure:
 * {{{
 * {
 *   "DisplayName":  "User Ingestion Pipeline",
 *   "Description": "Ingests and processes user data from source systems",
 *   "Id": "user-ingestion-pipeline"
 * }
 * }}}
 *
 * Fields:
 *  - DisplayName (String): A human-readable name for the pipeline
 *  - Description (String): A brief description of what the pipeline does
 *  - Id (String): A unique identifier for the pipeline
 *
 * @throws IllegalArgumentException if any required field is missing or invalid
 *                                  or if authentication parsing fails
 */
object DataPipelineMetadataParser extends Parser[DataPipelineMetadata] {
  /** Identifier for this parser, used in parser registries. */
  override def name: String = "Metadata"

  /**
   * Parses a [[DataPipelineMetadata]] from the given JSON cursor.
   *
   * @param cursor The Circe [[io.circe.ACursor]] pointing to the JSON object
   *               representing a [[DataPipelineMetadata]] configuration.
   * @return A fully constructed [[DataPipelineMetadata]] instance.
   *
   * @throws IllegalArgumentException if:
   *                                  - Any required field is missing
   *                                  - Any field has an invalid type
   *                                  - The Auth sub-field cannot be parsed
   */
  override def parse(cursor: ACursor): DataPipelineMetadata = {
    val displayName = cursor.get[String]("DisplayName") match {
      case Right(displayName) => displayName
      case Left(error) => throw new IllegalArgumentException(
        s"[DataPipelineMetadataParser] Missing or invalid 'DisplayName' field (expected Int): ${error.getMessage}"
      )
    }

    val description = cursor.get[String]("Description") match {
      case Right(description) => description
      case Left(error) => throw new IllegalArgumentException(
        s"[DataPipelineMetadataParser] Missing or invalid 'Description' field (expected Int): ${error.getMessage}"
      )
    }

    val id = cursor.get[String]("Id") match {
      case Right(id) => id
      case Left(error) => throw new IllegalArgumentException(
        s"[DataPipelineMetadataParser] Missing or invalid 'Id' field (expected Int): ${error.getMessage}"
      )
    }

    DataPipelineMetadata(
      displayName = displayName,
      description = description,
      id = id
    )
  }
}
