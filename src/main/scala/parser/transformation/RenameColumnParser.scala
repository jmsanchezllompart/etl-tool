package parser.transformation

import core.transformation.{RenameColumn, Transformation}
import io.circe.ACursor
import parser.Parser

object RenameColumnParser extends Parser[Transformation] {
  override val name: String = "RenameColumn"

  override def parse(cursor: ACursor): Transformation = {
    val oldName = cursor.get[String]("OldColumnName") match {
      case Right(oldName) => oldName
      case Left(error) => throw new IllegalArgumentException(
        s"[RenameColumnParser] Missing or invalid 'OldColumnName' field: ${error.getMessage}"
      )
    }

    val newName = cursor.get[String]("NewColumnName") match {
      case Right(newName) => newName
      case Left(error) => throw new IllegalArgumentException(
        s"[RenameColumnParser] Missing or invalid 'NewColumnName' field: ${error.getMessage}"
      )
    }

    RenameColumn(
      oldName = oldName,
      newName = newName
    )
  }
}