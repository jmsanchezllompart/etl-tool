package parser

import io.circe.ACursor

trait Parser[T] {
  def name: String
  def parse(cursor: ACursor): T
}
