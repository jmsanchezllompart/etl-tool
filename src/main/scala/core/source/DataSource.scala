package core.source

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.SparkSession

/**
 * Represents a source of data that can be read into a [[org.apache.spark.sql.DataFrame]].
 *
 * Implementations of this trait should provide the logic for reading
 * data from various sources such as files, databases, or external services.
 */
trait DataSource {
  /**
   * Reads data from this data source into a [[org.apache.spark.sql.DataFrame]].
   *
   * This method requires an implicit SparkSession to be in scope.
   * Each implementation should define how the data is read and
   * transformed into a [[org.apache.spark.sql.DataFrame]].
   *
   * @param sparkSession the implicit [[org.apache.spark.sql.SparkSession]] used to read the data
   * @return a [[org.apache.spark.sql.DataFrame]] containing the data from this source
   */
  def read()(implicit sparkSession: SparkSession): DataFrame
}
