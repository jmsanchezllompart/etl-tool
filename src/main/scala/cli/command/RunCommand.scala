package cli.command

import scopt.OParser

/**
 * Configuration for the `run` command.
 *
 * @param yamlFile Path to the YAML file containing the pipeline definition
 *                 to be executed.
 */
case class RunCommandConfig(yamlFile: String) extends BaseCommandConfig

/**
 * CLI command responsible for executing a pipeline defined in a YAML file.
 *
 * This command parses the `--pipeline` (or `-p`) argument and produces
 * a [[RunCommandConfig]] instance containing the provided YAML file path.
 *
 * Example usage:
 * {{{
 *   my-app run --pipeline path/to/pipeline.yaml
 *   my-app run -p path/to/pipeline.yaml
 * }}}
 */
object RunCommand extends Command[Unit, BaseCommandConfig] {
  /**
   * Builds the scopt parser for the `run` command.
   *
   * The parser:
   *  - defines the `run` subcommand
   *  - requires a `--pipeline` (or `-p`) argument
   *  - maps the input into a [[RunCommandConfig]] instance
   *
   * @return an [[scopt.OParser]] configured for the `run` command
   */
  override def build(): OParser[Unit, BaseCommandConfig] = {
    val builder = OParser.builder[BaseCommandConfig]
    import builder._

    builder
      .cmd("run")
      .text("run is the command used to execute the pipeline specified in the provided file")
      .children(
        opt[String]("pipeline")
          .abbr("p")
          .action((x, _) => RunCommandConfig(yamlFile = x))
          .text("Config file where the pipelines are stored")
          .required(),
      )
  }
}