package core.sink

import core.auth.{Auth, ServiceAccountAuth}
import org.apache.spark.sql.{DataFrame, SparkSession}

case class BigQuerySink
(
  project: String,
  dataset: String,
  table: String,
  auth: Auth,
  appendMode: AppendMode
) extends DataSink {
  override def write(source: DataFrame)(implicit sparkSession: SparkSession): Unit = {
    val credentialsFile = auth match {
      case ServiceAccountAuth(serviceAccountFile) => serviceAccountFile
      case _ => throw new UnsupportedOperationException("Unsupported auth method provided")
    }

    appendMode match {
      case Append() => append(source, credentialsFile)
      case Overwrite() => overwrite(source, credentialsFile)
      case Merge(idFields) => merge(source, idFields, credentialsFile)
    }
  }

  private def append(source: DataFrame, credentialsFile: String)(implicit sparkSession: SparkSession): Unit = {
    val destTable = s"$project:$dataset.$table"

    source.write
      .format("bigquery")
      .option("credentialsFile", credentialsFile)
      .option("writeMethod", "direct")
      .option("table", destTable)
      .mode("append")
      .save()
  }

  private def overwrite(source: DataFrame, credentialsFile: String)(implicit sparkSession: SparkSession): Unit = {
    val destTable = s"$project:$dataset.$table"

    source.write
      .format("bigquery")
      .option("credentialsFile", credentialsFile)
      .option("writeMethod", "direct")
      .option("table", destTable)
      .mode("overwrite")
      .option("writeDisposition", "WRITE_TRUNCATE")
      .save()
  }

  private def merge(source: DataFrame, idFields: List[String], credentialsFile: String)(implicit sparkSession: SparkSession): Unit = {
    val destTable = s"$project:$dataset.$table"
    val tempTable = s"$project:$dataset.${table}_temp_${System.currentTimeMillis()}"

    // Write contents to temporal table
    source.write
      .format("bigquery")
      .option("credentialsFile", credentialsFile)
      .option("writeMethod", "direct")
      .option("table", tempTable)
      .mode("overwrite")
      .save()

    // Perform merge query to final table
    val mergeQuery =
      s"""
         |MERGE `$destTable` T
         |USING `$tempTable` S
         |ON ${idFields.map(field => s"T.$field = S.$field").mkString(", ")}
         |WHEN MATCHED THEN UPDATE SET *
         |WHEN NOT MATCHED THEN INSERT ROW
         |""".stripMargin

    sparkSession.read
      .format("bigquery")
      .option("credentialsFile", credentialsFile)
      .option("query", mergeQuery)
      .load()

    // Drop temporal table
    sparkSession.read
      .format("bigquery")
      .option("credentialsFile", credentialsFile)
      .option("query", s"DROP TABLE `$tempTable`")
      .load()
  }
}