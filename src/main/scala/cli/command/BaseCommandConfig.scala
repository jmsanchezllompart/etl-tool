package cli.command

/**
 * Marker trait for command configuration objects.
 *
 * All CLI command configuration case classes should extend this trait
 * to provide a common type bound across the application.
 *
 * This enables:
 *  - grouping different command configs under a shared abstraction
 *  - easier handling of commands in a generic way
 *  - improved type safety when working with CLI parsing results
 *
 * Typically, each command will define its own case class extending this trait.
 *
 * Example:
 * {{{
 * case class MyCommandConfig(input: String, verbose: Boolean) extends BaseCommandConfig
 * }}}
 */
trait BaseCommandConfig