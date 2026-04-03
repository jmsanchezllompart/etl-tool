package core.sink

import core.auth.{Auth, ServiceAccountAuth}
import org.apache.spark.sql.{DataFrame, DataFrameWriter, SparkSession}
import org.mockito.ArgumentMatchers._
import org.mockito.MockitoSugar
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class BigQuerySinkSpec extends AnyFlatSpec with Matchers with MockitoSugar {
  implicit val spark: SparkSession =
    SparkSession.builder()
      .master("local[1]")
      .appName("BigQuerySinkTest")
      .getOrCreate()

  private def mockDataFrame(): DataFrame = {
    val df = mock[DataFrame]
    val writer = mock[DataFrameWriter[org.apache.spark.sql.Row]]

    when(df.write).thenReturn(writer)
    when(writer.format(any[String])).thenReturn(writer)
    when(writer.option(any[String], any[String])).thenReturn(writer)
    when(writer.mode(any[String])).thenReturn(writer)

    df
  }

  "BigQuerySink" should "throw UnsupportedOperationException when unsupported Auth is provided" in {
    val df = mockDataFrame()

    val sink = BigQuerySink(
      "project",
      "dataset",
      "table",
      auth = new Auth {}, // unsupported
      appendMode = Append()
    )

    assertThrows[UnsupportedOperationException] {
      sink.write(df)
    }
  }

  it should "append when write is done in Append Mode" in {
    val df = mockDataFrame()
    val writer = df.write

    val sink = BigQuerySink(
      "project",
      "dataset",
      "table",
      ServiceAccountAuth("/tmp/key.json"),
      Append()
    )

    sink.write(df)

    verify(writer).mode("append")
    verify(writer).format("bigquery")
    verify(writer).option("credentialsFile", "/tmp/key.json")
    verify(writer).option("table", "project:dataset.table")
    verify(writer).save()
  }

  it should "overwrite and truncate when is done in Overwrite Mode" in {
    val df = mockDataFrame()
    val writer = df.write

    val sink = BigQuerySink(
      "project",
      "dataset",
      "table",
      ServiceAccountAuth("/tmp/key.json"),
      Overwrite()
    )

    sink.write(df)

    verify(writer).mode("overwrite")
    verify(writer).option("writeDisposition", "WRITE_TRUNCATE")
    verify(writer).option("table", "project:dataset.table")
    verify(writer).save()
  }
}
