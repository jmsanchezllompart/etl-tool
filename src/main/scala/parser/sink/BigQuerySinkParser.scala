package parser.sink

import core.auth.Auth
import core.sink.{Append, BigQuerySink, DataSink}
import io.circe.ACursor
import parser.Parser
import parser.auth.AuthParserRegistry
import parser.helpers.Helpers.parseSubField


object BigQuerySinkParser extends Parser[DataSink] {
  override def name: String = "BigQuerySink"

  override def parse(cursor: ACursor): DataSink = {
    val project = cursor.get[String]("Project") match {
      case Right(project) => project
      case Left(_) => throw new Exception()
    }

    val dataset = cursor.get[String]("Dataset") match {
      case Right(dataset) => dataset
      case Left(_) => throw new Exception()
    }

    val table = cursor.get[String]("Table") match {
      case Right(table) => table
      case Left(_) => throw new Exception()
    }

    val auth = parseSubField[Auth](cursor = cursor, parserRegistry = AuthParserRegistry, fieldKey = "Auth")

    BigQuerySink(
      project = project,
      dataset = dataset,
      table = table,
      auth = auth,
      appendMode = Append() // TODO: Parse properly
    )
  }
}
