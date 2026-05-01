package cli.command

import scopt.OParser

/**
 * Configuration for the `check` command.
 *
 * @param yamlFile Path to the YAML file containing the pipeline definition
 *                 to be validated.
 */
case class CheckCommandConfig(yamlFile: String) extends BaseCommandConfig

/**
 * CLI command responsible for validating a pipeline configuration file.
 *
 * This command parses the `--pipeline` (or `-p`) argument and produces
 * a [[CheckCommandConfig]] instance containing the provided YAML file path.
 *
 * Example usage:
 * {{{
 *   my-app check --pipeline path/to/pipeline.yaml
 *   my-app check -p path/to/pipeline.yaml
 * }}}
 */
object CheckCommand extends Command[Unit, BaseCommandConfig] {

  /**
   * Builds the scopt parser for the `check` command.
   *
   * The parser:
   *  - defines the `check` subcommand
   *  - requires a `--pipeline` (or `-p`) argument
   *  - maps the input into a [[CheckCommandConfig]] instance
   *
   * @return an [[scopt.OParser]] configured for the `check` command
   */
  override def build(): OParser[Unit, BaseCommandConfig] = {
    val builder = OParser.builder[BaseCommandConfig]
    import builder._

    builder
      .cmd("check")
      .text("check is the command used to check if the provided pipeline is in a valid format")
      .children(
        opt[String]("pipeline")
          .abbr("p")
          .action((x, _) => CheckCommandConfig(yamlFile = x))
          .text("Config file where the pipelines are stored")
          .required(),
      )
  }
}