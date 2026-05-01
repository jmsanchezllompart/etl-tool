package cli.command

import scopt.OParser

/**
 * Base trait for defining CLI commands using scopt.
 *
 * A command is responsible for building an [[scopt.OParser]] instance
 * that describes how command-line arguments are parsed into a configuration object.
 *
 * @tparam A The type of the parsed command-line arguments (typically the options model).
 * @tparam C The type of the resulting configuration object produced after parsing.
 */
trait Command[A, C] {
  /**
   * Builds the scopt parser for this command.
   *
   * The parser defines:
   *  - available options and flags
   *  - argument structure
   *  - validation rules
   *
   * @return an [[scopt.OParser]] instance configured for this command
   */
  def build(): OParser[A, C]
}