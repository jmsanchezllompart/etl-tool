package core.transformation

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.classic.SparkSession

/**
 * A transformation that renames a column in a [[DataFrame]].
 *
 * This is a simple implementation of the [[Transformation]] trait that
 * updates the name of an existing column while preserving its data.
 *
 * If the specified column does not exist, Spark will return the
 * original [[DataFrame]] unchanged.
 *
 * @param oldName the current name of the column
 * @param newName the new name to assign to the column
 * @example
 * {{{
 *   val transformation = RenameColumn("old_name", "new_name")
 *   val result = transformation.transform(df)
 * }}}
 */
case class RenameColumn(oldName: String, newName: String) extends Transformation {
  /**
   * Renames a column in the input [[DataFrame]].
   *
   * @param source the input [[DataFrame]]
   * @param sparkSession the implicit [[SparkSession]]
   * @return a new [[DataFrame]] with the column renamed
   */
  override def transform(source: DataFrame)(implicit sparkSession: SparkSession): DataFrame =
    source.withColumnRenamed(oldName, newName)
}
