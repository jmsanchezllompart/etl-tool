package core.pipeline

import core.sink.DataSink
import core.source.DataSource
import core.transformation.Transformation

case class DataPipelineMetadata
(
  displayName: String,
  description: String,
  id: String
)

case class DataPipeline
(
  metadata: DataPipelineMetadata,
  source: DataSource,
  transformations: List[Transformation],
  sink: DataSink
)