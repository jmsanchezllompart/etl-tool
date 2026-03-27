package core.transformation

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.classic.SparkSession

trait Transformation {
  def transform(source: DataFrame)(implicit sparkSession: SparkSession): DataFrame
}
