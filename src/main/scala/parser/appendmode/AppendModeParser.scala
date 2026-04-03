package parser.appendmode

import core.sink.{Append, AppendMode}
import io.circe.ACursor
import parser.Parser

object AppendModeParser extends Parser[AppendMode] {
  override def name: String = "Append"

  override def parse(cursor: ACursor): AppendMode = {
    Append()
  }
}
