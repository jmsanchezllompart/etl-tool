package parser.helpers

import io.circe.ACursor
import parser.ParserRegistry

object Helpers {
  def parseSubField[T](cursor: ACursor, parserRegistry: ParserRegistry[T], fieldKey: String): T = {
    val subCursor = cursor.downField(fieldKey)
    val tTypes = subCursor.keys.getOrElse(throw new IllegalArgumentException("Invalid YAML format"))

    val tType = if (tTypes.size == 1) {
      tTypes.head
    } else if (tTypes.size > 1) {
      throw new IllegalArgumentException(s"Multiple ${fieldKey} found, please choose only one")
    } else {
      throw new IllegalArgumentException(s"At least one ${fieldKey} must be provided")
    }

    val parser = parserRegistry.get(tType)
    parser.parse(subCursor.downField(tType))
  }

  def parseSubFieldsList[T](cursor: ACursor, parserRegistry: ParserRegistry[T], fieldKey: String): List[T] = {
    val values = cursor.downField(fieldKey).values.get

    values.map(value => {
      val subCursor = value.hcursor
      val key = subCursor.keys.get.head
      val parser = parserRegistry.get(key)
      parser.parse(subCursor.downField(key))
    }).toList
  }
}

