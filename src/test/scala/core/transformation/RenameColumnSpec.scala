package core.transformation

import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class RenameColumnSpec extends AnyFlatSpec with Matchers {
  implicit val spark: SparkSession =
    SparkSession.builder()
      .master("local[1]")
      .appName("RenameColumnTest")
      .getOrCreate()

  private def createDF(schema: StructType, data: Seq[Row]): DataFrame =
    spark.createDataFrame(
      spark.sparkContext.parallelize(data),
      schema
    )

  "RenameColumn" should "not modify any field when originalName is not present in the original DataFrame" in {
    val schema = StructType(Seq(
      StructField("id", IntegerType, nullable = false),
      StructField("name", StringType, nullable = true)
    ))

    val df = createDF(schema, Seq(Row(1, "Paco")))

    val transformation = RenameColumn("missing_column", "new_name")

    val transformedDf = transformation.transform(df)

    assert(transformedDf.schema == df.schema)
  }

  it should "only correctly rename the column when the field is present in the original schema" in {
    val schema = StructType(Seq(
      StructField("id", IntegerType, nullable = false),
      StructField("name", StringType, nullable = true)
    ))

    val expectedSchema = StructType(Seq(
      StructField("id", IntegerType, nullable = false),
      StructField("newName", StringType, nullable = true)
    ))

    val df = createDF(schema, Seq(Row(1, "Paco")))

    val transformation = RenameColumn("name", "newName")

    val transformedDf = transformation.transform(df)

    assert(transformedDf.schema == expectedSchema)
  }
}
