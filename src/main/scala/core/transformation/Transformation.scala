package core.transformation

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * Represents a transformation applied to a [[DataFrame]].
 *
 * Implementations of this trait define reusable data processing logic,
 * such as filtering, aggregations, joins, or schema transformations.
 *
 * This abstraction allows transformations to be composed and reused
 * across different data pipelines.
 */
trait Transformation {
  /**
   * Transforms the input [[DataFrame]] into a new [[DataFrame]].
   *
   * This method requires an implicit [[SparkSession]] to be in scope.
   * Implementations should define the transformation logic while
   * keeping the input DataFrame immutable.
   *
   * @param source the input [[DataFrame]] to transform
   * @param sparkSession the implicit [[SparkSession]]
   * @return a new [[DataFrame]] resulting from applying the transformation
   */
  def transform(source: DataFrame)(implicit sparkSession: SparkSession): DataFrame
}
