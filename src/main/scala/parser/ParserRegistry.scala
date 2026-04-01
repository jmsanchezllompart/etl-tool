package parser

/**
 * Represents a registry of parsers that can be retrieved by name.
 *
 * This trait provides a mechanism for looking up [[Parser]] instances
 * dynamically, typically based on a string identifier. It is useful
 * for decoupling parser selection from implementation details and
 * enabling flexible, configurable parsing logic.
 *
 * @tparam T the type of value produced by the registered parsers
 */
trait ParserRegistry[T] {
  /**
   * Retrieves a parser by its name.
   *
   * Implementations should define how parsers are stored and
   * resolved (e.g., in-memory map, dependency injection, etc.).
   *
   * @param name the name of the parser to retrieve
   * @return the corresponding [[Parser]]
   * @throws IllegalArgumentException if no parser with the given name exists
   */
  def get(name: String): Parser[T]
}
