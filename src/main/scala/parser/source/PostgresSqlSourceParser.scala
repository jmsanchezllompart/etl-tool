package parser.source

import core.auth.Auth
import core.source.{DataSource, PostgresSqlSource}
import io.circe.ACursor
import parser.Parser
import parser.auth.AuthParserRegistry
import parser.helpers.Helpers.parseSubField

object PostgresSqlSourceParser extends Parser[DataSource] {
  override def name: String = "PostgresSqlSource"

  override def parse(cursor: ACursor): DataSource = {
    val host = cursor.get[String]("Host") match {
      case Right(host) => host
      case Left(_) => throw new Exception()
    }

    val port = cursor.get[Int]("Port") match {
      case Right(port) => port.toString
      case Left(_) => throw new Exception()
    }

    val database = cursor.get[String]("Database") match {
      case Right(database) => database
      case Left(_) => throw new Exception()
    }

    // Auth parsing
    val auth = parseSubField[Auth](cursor = cursor, parserRegistry = AuthParserRegistry, fieldKey = "Auth")

    val query = cursor.get[String]("RawQuery") match {
      case Right(query) => query
      case Left(_) => throw new Exception()
    }

    PostgresSqlSource(
      host = host,
      port = port,
      database = database,
      auth = auth,
      query = query
    )
  }
}
