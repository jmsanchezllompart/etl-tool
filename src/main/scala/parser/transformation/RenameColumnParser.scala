package parser.transformation

import core.transformation.{RenameColumn, Transformation}
import io.circe.ACursor
import parser.Parser

object RenameColumnParser extends Parser[Transformation] {
  override val name: String = "RenameColumn"

  override def parse(cursor: ACursor): Transformation = {
    val oldName = cursor.get[String]("OldColumnName") match {
      case Right(oldName) => oldName
      case Left(_) => throw new Exception()
    }

    val newName = cursor.get[String]("NewColumnName") match {
      case Right(newName) => newName
      case Left(_) => throw new Exception()
    }

    RenameColumn(
      oldName = oldName,
      newName = newName
    )
  }
}