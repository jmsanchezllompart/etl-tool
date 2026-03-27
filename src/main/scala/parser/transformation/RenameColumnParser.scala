package parser.transformation

import core.transformation.{RenameColumn, Transformation}

import scala.jdk.CollectionConverters.MapHasAsScala

object RenameColumnParser extends TransformationParser {
  override val name: String = "RenameColumn"

  override def parse(value: Any): Transformation = {
    val map = value
      .asInstanceOf[java.util.Map[String, Object]]
      .asScala

    RenameColumn(
      oldName = map("OldColumnName").toString,
      newName = map("NewColumnName").toString
    )
  }
}