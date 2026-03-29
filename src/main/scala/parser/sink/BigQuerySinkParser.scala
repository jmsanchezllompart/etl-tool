package parser.sink

import core.sink.{Append, AppendMode, BigQuerySink, DataSink}
import parser.auth.AuthParserRegistry

import scala.jdk.CollectionConverters.MapHasAsScala

object BigQuerySinkParser extends DataSinkParser {
  override def name: String = "BigQuerySink"

  override def parse(value: Any): DataSink = {
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

    BigQuerySink(
      project = map("Project").toString,
      dataset = map("Dataset").toString,
      table = map("Table").toString,
      auth = auth,
      appendMode = Append() // TODO: Parse properly
    )
  }
}
