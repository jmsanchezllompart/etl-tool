package core.source

import core.auth.{Auth, BasicAuth}
import org.apache.spark.sql.{DataFrame, DataFrameReader, SparkSession}
import org.mockito.ArgumentMatchers.anyString
import org.scalatest.flatspec.AnyFlatSpec
import org.mockito.Mockito._
import org.scalatest.matchers.should.Matchers

class SqlServerSourceSpec extends AnyFlatSpec with Matchers {
  "SqlServerSource" should "build correct JDBC options and call load" in {
    val spark = mock(classOf[SparkSession])
    val reader = mock(classOf[DataFrameReader])
    val df = mock(classOf[DataFrame])

    when(spark.read).thenReturn(reader)
    when(reader.format("jdbc")).thenReturn(reader)
    when(reader.option(anyString(), anyString())).thenReturn(reader)
    when(reader.load()).thenReturn(df)

    val source = SqlServerSource(
      host = "localhost",
      port = "1433",
      database = "test_db",
      auth = BasicAuth("user", "pass"),
      query = "SELECT * FROM users"
    )

    val result = source.read()(spark)

    result shouldBe df

    verify(reader).format("jdbc")
    verify(reader).option("url", "jdbc:sqlserver://localhost:1433;databaseName=test_db")
    verify(reader).option("user", "user")
    verify(reader).option("password", "pass")
    verify(reader).option("driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver")
    verify(reader).option("dbtable", "(SELECT * FROM users) as subquery")
    verify(reader).load()
  }

  it should "throw exception for unsupported auth" in {
    val spark = mock(classOf[SparkSession])

    val source = SqlServerSource(
      host = "localhost",
      port = "1433",
      database = "test_db",
      auth = new Auth {}, // fake unsupported auth
      query = "SELECT 1"
    )

    assertThrows[UnsupportedOperationException] {
      source.read()(spark)
    }
  }
}