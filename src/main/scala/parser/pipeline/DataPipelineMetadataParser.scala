package parser.pipeline

import core.pipeline.DataPipelineMetadata
import io.circe.ACursor
import parser.Parser

object DataPipelineMetadataParser extends Parser[DataPipelineMetadata] {

  override def name: String = "Metadata"

  override def parse(cursor: ACursor): DataPipelineMetadata = {
    val displayName = cursor.get[String]("DisplayName") match {
      case Right(displayName) => displayName
      case Left(_) => throw new Exception()
    }

    val description = cursor.get[String]("Description") match {
      case Right(description) => description
      case Left(_) => throw new Exception()
    }

    val id = cursor.get[String]("Id") match {
      case Right(id) => id
      case Left(_) => throw new Exception()
    }

    DataPipelineMetadata(
      displayName = displayName,
      description = description,
      id = id
    )
  }
}
