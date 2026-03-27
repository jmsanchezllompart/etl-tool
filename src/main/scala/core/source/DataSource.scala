package core.source

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.SparkSession

trait DataSource {
  def read()(implicit sparkSession: SparkSession): DataFrame
}
