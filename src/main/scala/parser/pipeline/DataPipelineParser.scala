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

object DataPipelineParser extends Parser[DataPipeline] {
  override def name: String = "BasicDataPipeline"

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
