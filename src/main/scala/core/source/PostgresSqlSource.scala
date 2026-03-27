package core.source

import core.auth.{Auth, BasicAuth, ServiceAccountFile}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.SparkSession

case class PostgresSqlSource
(
  host: String,
  port: String,
  database: String,
  auth: Auth,
  query: String
) extends DataSource {
  override def read()(implicit sparkSession: SparkSession): DataFrame = {
    val jdbcUrl = s"jdbc:postgresql://$host:$port/$database"

    val (user, password) = auth match {
      case BasicAuth(user, password) => (user, password)
      case _ => throw new UnsupportedOperationException(s"Unsupported auth method for PostgresSQL Data Source")
    }

    sparkSession.read
      .format("jdbc")
      .option("url", jdbcUrl)
      .option("driver", "org.postgresql.Driver")
      .option("user", user)
      .option("password", password)
      .option("dbtable", s"($query) AS subquery") // Important: wrap query as subquery
      .load()
  }
}
