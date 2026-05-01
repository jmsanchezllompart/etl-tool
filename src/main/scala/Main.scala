import cli.command.{BaseCommandConfig, CheckCommand, CheckCommandConfig, RunCommand, RunCommandConfig}
import scopt.OParser
import io.circe.yaml
import org.apache.spark.sql.SparkSession
import parser.pipeline.DataPipelineParser

import scala.io.Source

case class Config() extends BaseCommandConfig

object Main {

  def main(args: Array[String]): Unit = {
    val builder = OParser.builder[BaseCommandConfig]
    val parser = {
      import builder._
      OParser.sequence(
        programName("etl-tool"),
        head("etl-tool", "0.1.0-SNAPSHOT"),
        RunCommand.build(),
        CheckCommand.build()
      )
    }

    // OParser.parse returns Option[Config]
    OParser.parse(parser, args, Config()).get match {
      case RunCommandConfig(yamlFile) =>
        implicit val sparkSession: SparkSession = SparkSession.builder()
          .appName("etl-tool")
          .master("local[*]") // run locally using all cores
          .getOrCreate()
        val yamlSource = Source.fromFile(yamlFile)
        val json = yaml.parser.parse(yamlSource.mkString).getOrElse(throw new Exception)
        yamlSource.close()
        val pipeline = DataPipelineParser.parse(json.hcursor.root)
        pipeline.run()

      case CheckCommandConfig(yamlFile) =>
        val yamlSource = Source.fromFile(yamlFile)
        val json = yaml.parser.parse(yamlSource.mkString).getOrElse(throw new Exception)
        yamlSource.close()
        DataPipelineParser.parse(json.hcursor.root)
      case _ =>
      // arguments are bad, error message will have been displayed
    }
  }
}

