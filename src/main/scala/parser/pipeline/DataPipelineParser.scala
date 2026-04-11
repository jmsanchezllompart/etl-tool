package parser.pipeline

import core.pipeline.DataPipeline
import core.sink.DataSink
import core.source.DataSource
import core.transformation.Transformation
import io.circe.ACursor
import parser.Parser
import parser.helpers.Helpers.{parseSubField, parseSubFieldsList}
import parser.sink.DataSinkParserRegistry
import parser.source.DataSourceParserRegistry
import parser.transformation.TransformationParserRegistry

/**
 * Parser implementation for constructing a [[DataPipeline]] from a JSON configuration.
 *
 * This parser reads required fields from a Circe [[io.circe.ACursor]] and converts them
 * into a fully configured [[DataPipeline]] instance.
 *
 * Expected JSON structure:
 * {{{
 * {
 *   "Metadata":  { ... },
 *   "DataSource": { ... },
 *   "Transformations": { ... },
 *   "DataSink": { ... }
 * }
 * }}}
 *
 * Fields:
 *  - Metadata (Object): Data pipeline meta-data
 *  - DataSource (Object): Data source where the pipeline reads from
 *  - Transformations (Object): An array containing the transformations applied during the pipeline processing
 *  - DataSink (Object): Data sink where the processed data is written to
 *
 * @throws IllegalArgumentException if any required field is missing or invalid
 *                                  or if authentication parsing fails
 */
object DataPipelineParser extends Parser[DataPipeline] {
  /** Identifier for this parser, used in parser registries. */
  override def name: String = "BasicDataPipeline"

  /**
   * Parses a [[DataPipeline]] from the given JSON cursor.
   *
   * @param cursor The Circe [[io.circe.ACursor]] pointing to the JSON object
   *               representing a [[DataPipeline]] configuration.
   * @return A fully constructed [[DataPipeline]] instance.
   *
   * @throws IllegalArgumentException if:
   *                                  - Any required field is missing
   *                                  - Any field has an invalid type
   *                                  - The Auth sub-field cannot be parsed
   */
  override def parse(cursor: ACursor): DataPipeline = {
    val metadata = DataPipelineMetadataParser.parse(cursor.downField("Metadata"))
    val source = parseSubField[DataSource](cursor = cursor, parserRegistry = DataSourceParserRegistry, fieldKey = "DataSource")
    val transformations = parseSubFieldsList[Transformation](cursor = cursor, TransformationParserRegistry, fieldKey = "Transformations")
    val sink = parseSubField[DataSink](cursor = cursor, parserRegistry = DataSinkParserRegistry, fieldKey = "DataSink")
    DataPipeline(
      metadata = metadata,
      source = source,
      transformations = transformations,
      sink = sink
    )
  }
}
