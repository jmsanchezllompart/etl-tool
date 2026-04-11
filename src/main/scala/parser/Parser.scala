package parser

import io.circe.ACursor

/**
 * Represents a parser that extracts a value of type `T` from a JSON cursor.
 *
 * Implementations of this trait define how to read and convert data
 * from a given [[ACursor]] into a strongly-typed value.
 *
 * This abstraction is useful for building reusable and composable
 * parsing logic, especially when working with JSON structures.
 *
 * @tparam T the type of value produced by this parser
 */
trait Parser[T] {
  /**
   * The name of the parser.
   *
   * This can be used for identification, logging, or debugging purposes.
   *
   * @return a human-readable name for the parser
   */
  def name: String

  /**
   * Parses a value of type `T` from the given JSON cursor.
   *
   * Implementations should define how to navigate the cursor
   * and extract the desired value.
   *
   * @param cursor [[ACursor]] pointing to the data to parse
   * @return the parsed value of type `T`
   */
  def parse(cursor: ACursor): T
}
