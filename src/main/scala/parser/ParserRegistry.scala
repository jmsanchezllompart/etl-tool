package parser

trait ParserRegistry[T] {
  def get(name: String): Parser[T]
}
