package core.sink

import core.auth.{Auth, ServiceAccountAuth}
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * A [[DataSink]] implementation for writing [[org.apache.spark.sql.DataFrame]] to Google BigQuery.
 *
 * This sink persists the specified [[org.apache.spark.sql.DataFrame]] to BigQuery using the specified config.
 *
 * @param project the GCP project ID
 * @param dataset the BigQuery dataset name
 * @param table the BigQuery table name
 * @param auth authentication method (must extend [[core.auth.Auth]], currently only
 *             [[core.auth.ServiceAccountAuth]] is supported)
 * @param appendMode specifies how to Append the data to the specified BigQuery table.
 *                   It must extend [[core.sink.AppendMode]] and it supports `Append`, `Overwrite` and `Merge` modes
 * @example
 * {{{
 * val sink = BigQuerySink(
 *   project = "test-project",
 *   dataset = "test-dataset",
 *   table = "test-table",
 *   auth = ServiceAccountAuth(
 *     serviceAccountFile = "/path/to/service-account.json"
 *   ),
 *   appendMode = Append()
 * )
 *
 * sink.write(df)
 * }}}
 *
 */
case class BigQuerySink
(
  project: String,
  dataset: String,
  table: String,
  auth: Auth,
  appendMode: AppendMode
) extends DataSink {

  /**
   * Writes the given DataFrame to BigQuery using the configured authentication
   * and write mode.
   *
   * The method handles different `AppendMode`s:
   *   - `Append`: appends data to the table.
   *   - `Overwrite`: overwrites existing data.
   *   - `Merge`: merges new data with existing data based on key fields.
   *
   * @param source the DataFrame to write
   * @param sparkSession the implicit SparkSession
   * @throws UnsupportedOperationException if an unsupported Auth type is provided
   */
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

  /**
   * Appends the [[DataFrame]] to the BigQuery table.
   *
   * @param source the [[DataFrame]] to append
   * @param credentialsFile the path to the service account credentials
   * @param sparkSession the implicit [[SparkSession]]
   */
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

  /**
   * Overwrites the BigQuery table with the DataFrame.
   *
   * @param source the [[DataFrame]] to write
   * @param credentialsFile the path to the service account credentials
   * @param sparkSession the implicit [[SparkSession]]
   */
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

  /**
   * Merges the [[DataFrame]] with the existing BigQuery table based on key fields.
   *
   * This method creates a temporary table for the new data, performs a merge
   * using the provided ID fields, and drops the temporary table afterward.
   *
   * @param source the [[DataFrame]] to merge
   * @param idFields the fields used to match records for merging
   * @param credentialsFile the path to the service account credentials
   * @param sparkSession the implicit [[SparkSession]]
   */
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