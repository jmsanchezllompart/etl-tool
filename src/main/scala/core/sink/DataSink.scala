package core.sink

import org.apache.spark.sql.{DataFrame, SparkSession}

trait DataSink {
  def write(source: DataFrame)(implicit sparkSession: SparkSession): Unit
}
