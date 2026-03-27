package core.transformation

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.classic.SparkSession

case class RenameColumn(oldName: String, newName: String) extends Transformation {
  override def transform(source: DataFrame)(implicit sparkSession: SparkSession): DataFrame =
    source.withColumnRenamed(oldName, newName)
}
