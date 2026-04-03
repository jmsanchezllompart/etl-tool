package parser.appendmode

import core.sink.{AppendMode, Overwrite}
import io.circe.ACursor
import parser.Parser

object OverwriteModeParser extends Parser[AppendMode] {
  override def name: String = "Overwrite"

  override def parse(cursor: ACursor): AppendMode = {
    Overwrite()
  }
}
