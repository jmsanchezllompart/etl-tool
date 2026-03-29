package core.sink

trait AppendMode

case class Append() extends AppendMode
case class Overwrite() extends AppendMode
case class Merge(idFields: List[String]) extends AppendMode