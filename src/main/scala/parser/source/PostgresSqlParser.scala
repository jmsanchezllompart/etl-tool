package parser.source

import core.source.{DataSource, PostgresSqlSource}
import parser.auth.AuthParserRegistry

import scala.jdk.CollectionConverters.MapHasAsScala

object PostgresSqlParser extends DataSourceParser {
  override def name: String = "PostgresSqlSource"

  override def parse(value: Any): DataSource = {
    val map = value
      .asInstanceOf[java.util.Map[String, Object]]
      .asScala

    val authMap = map
      .getOrElse(
        "Auth",
        throw new IllegalArgumentException("Auth method must be provided")
      )
      .asInstanceOf[java.util.Map[String, Object]]
      .asScala

    val auth = if (authMap.keys.isEmpty) {
      throw new IllegalArgumentException("At least one auth method must be provided")
    } else if (authMap.keys.size > 1) {
      throw new IllegalArgumentException("Multiple auth methods were found")
    } else {
      val authMethod = authMap.keys.head
      AuthParserRegistry.get(authMethod).parse(authMap.head)
    }

    PostgresSqlSource(
      host = map("Host").toString,
      port = map("Port").toString,
      database = map("Database").toString,
      auth = auth,
      query = map("RawQuery").toString
    )
  }
}
