package parser.transformation

object TransformationParserRegistry {

  private val parsers: Map[String, TransformationParser] = List(
    RenameColumnParser
  ).map(p => p.name -> p).toMap

  def get(name: String): TransformationParser =
    parsers.getOrElse(
      name,
      throw new IllegalArgumentException(s"Unknown transformation: $name")
    )
}