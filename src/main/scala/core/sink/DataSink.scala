package core.sink

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * Represents a destination for writing data from a [[org.apache.spark.sql.DataFrame]].
 *
 * Implementations of this trait should provide the logic for persisting
 * data to various sinks such as files, databases, or external services.
 */
trait DataSink {
  /**
   * Writes the given [[org.apache.spark.sql.DataFrame]] to this data sink.
   *
   * This method requires an implicit [[org.apache.spark.sql.SparkSession]] to be in scope.
   * Each implementation should define how the data is written and
   * any necessary configuration (e.g., format, path, connection parameters).
   *
   * @param source the [[org.apache.spark.sql.DataFrame]] containing the data to be written
   * @param sparkSession the implicit [[org.apache.spark.sql.SparkSession]] used for writing
   */
  def write(source: DataFrame)(implicit sparkSession: SparkSession): Unit
}
