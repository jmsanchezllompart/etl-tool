package core.source

import core.auth.{Auth, BasicAuth}
import org.apache.spark.sql.{DataFrame, SparkSession}
/**
 * A [[DataSource]] implementation for reading data from a MySQL database using JDBC.
 *
 * This source executes a SQL query against a MySQL database and returns the result
 * as a Spark [[org.apache.spark.sql.DataFrame]].
 *
 * @param host     The hostname or IP address of the MySQL server.
 * @param port     The port on which the MySQL server is running (typically 3306).
 * @param database The name of the database to connect to.
 * @param auth     Authentication configuration. Currently only [[BasicAuth]] is supported.
 * @param query    The SQL query to execute. This will be wrapped as a subquery when passed to Spark.
 *
 * @example
 * {{{
 * implicit val spark: SparkSession = SparkSession.builder().getOrCreate()
 *
 * val source = MySqlSource(
 *   host = "localhost",
 *   port = "3306",
 *   database = "my_db",
 *   auth = BasicAuth("user", "password"),
 *   query = "SELECT * FROM users"
 * )
 *
 * val df = source.read()
 * df.show()
 * }}}
 */
case class MySqlSource
(
  host: String,
  port: String,
  database: String,
  auth: Auth,
  query: String
) extends DataSource {
  /**
   * Reads data from the configured MySQL source into a Spark [[org.apache.spark.sql.DataFrame]].
   *
   * This method constructs a JDBC connection URL and uses Spark's JDBC reader to execute
   * the provided SQL query. The query is wrapped as a subquery to allow arbitrary SQL.
   *
   * @param sparkSession An implicit [[org.apache.spark.sql.SparkSession]] used to perform the read.
   * @return A [[org.apache.spark.sql.DataFrame]] containing the query results.
   *
   * @throws UnsupportedOperationException if the provided authentication method is not supported.
   */
  override def read()(implicit sparkSession: SparkSession): DataFrame = {
    val jdbcUrl = s"jdbc:mysql://$host:$port/$database"

    val (user, password) = auth match {
      case BasicAuth(user, password) => (user, password)
      case _ => throw new UnsupportedOperationException(s"Unsupported auth method for PostgresSQL Data Source")
    }

    sparkSession.read
      .format("jdbc")
      .option("url", jdbcUrl)
      .option("driver", "com.mysql.cj.jdbc.Driver")
      .option("user", user)
      .option("password", password)
      .option("dbtable", s"($query) as subquery") // Important: wrap query as subquery
      .load()
  }
}
