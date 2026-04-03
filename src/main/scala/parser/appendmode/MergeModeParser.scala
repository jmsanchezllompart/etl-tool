package parser.appendmode

import core.sink.{AppendMode, Merge}
import io.circe.ACursor
import parser.Parser

object MergeModeParser extends Parser[AppendMode] {
  override def name: String = "Merge"

  override def parse(cursor: ACursor): AppendMode = {
    val idFields = cursor.get[List[String]]("IdFields") match {
      case Right(project) => project
      case Left(error) => throw new IllegalArgumentException(
        s"[MergeModeParser] Missing or invalid 'IdFields' field: ${error.getMessage}"
      )
    }

    Merge(idFields)
  }
}
