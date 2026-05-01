package core.pipeline

import core.sink.DataSink
import core.source.DataSource
import core.transformation.Transformation
import org.apache.spark.sql.SparkSession

/**
 * Represents a complete data pipeline.
 *
 * A data pipeline defines the flow of data from a source,
 * through a series of transformations, and into a destination (sink).
 *
 * This abstraction enables building modular, reusable, and
 * composable data processing workflows.
 *
 * The pipeline consists of:
 *   - a [[DataSource]] to read data
 *   - a sequence of [[Transformation]]s to process the data
 *   - a [[DataSink]] to write the final result
 *
 * Example flow:
 * {{{
 *   source -> transformation1 -> transformation2 -> sink
 * }}}
 *
 * @param metadata descriptive information about the pipeline
 * @param source the data source from which data is read
 * @param transformations the ordered list of transformations applied to the data
 * @param sink the destination where the processed data is written
 */
case class DataPipeline
(
  metadata: DataPipelineMetadata,
  source: DataSource,
  transformations: List[Transformation],
  sink: DataSink
) {
  def run()(implicit sparkSession: SparkSession): Unit = {
    print(s"Loading ${metadata.displayName} pipeline")

    val rawData = source.read()

    val transformedData = transformations.foldLeft(rawData) {
      (df, transformation) => transformation.transform(df)
    }

    sink.write(transformedData)
  }
}