import scopt.OParser
import java.io.File

case class Config
(
  foo: Int = -1,
  out: File = new File("."),
  configFile: String = "",
  libName: String = "",
  maxCount: Int = -1,
  verbose: Boolean = false,
  debug: Boolean = false,
  mode: String = "",
  files: Seq[File] = Seq(),
  keepalive: Boolean = false,
  jars: Seq[File] = Seq(),
  kwargs: Map[String, String] = Map()
)

object Main {
  def main(args: Array[String]): Unit = {
    val builder = OParser.builder[Config]
    val parser1 = {
      import builder._
      OParser.sequence(
        programName("etl-tool"),
        head("etl-tool", "0.1.0-SNAPSHOT"),
        help("help").text("prints this usage text"),
        note("some notes." + sys.props("line.separator")),
        cmd("run")
          .action((_, c) => c.copy(mode = "run"))
          .text("run is the command used to run the pipelines specified in the config file")
          .children(
            opt[String]("config-file")
              .abbr("cf")
              .action((x, c) => c.copy(configFile = x))
              .text("Config file where the pipelines are stored")
              .required(),
          ),
        cmd("check")
          .action((_, c) => c.copy(mode = "run"))
          .text("check is the command used to check if the provided config file has a valid format")
          .children(
            opt[String]("config-file")
              .abbr("cf")
              .action((x, c) => c.copy(configFile = x))
              .text("Config file where the pipelines are stored")
              .required(),
          ),
      )
    }

    // OParser.parse returns Option[Config]
    OParser.parse(parser1, args, Config()) match {
      case Some(config) =>
      // do something
      case _ =>
      // arguments are bad, error message will have been displayed
    }
  }
}

