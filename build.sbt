ThisBuild / version := "0.1.2-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.18"

lazy val root = (project in file("."))
  .settings(
    name := "etl-tool"
  )

val sparkVersion = "4.1.1"

libraryDependencies ++= Seq(
  // CLI Parameters
  "com.github.scopt" %% "scopt" % "4.1.0",
  // JSON Parsing
  "io.circe" %% "circe-parser" % "0.14.15",
  // YAML Parsing
  "io.circe" %% "circe-yaml" % "1.15.0",
  // Spark
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-mllib" % sparkVersion,
  // PostgresSql
  "org.postgresql" % "postgresql" % "42.7.10",
  // MySql
  "com.mysql" % "mysql-connector-j" % "9.6.0",
  // SQL Server
  "com.microsoft.sqlserver" % "mssql-jdbc" % "13.4.0.jre11",
  // Tests
  "org.scalatest" %% "scalatest" % "3.2.20" % Test,
  "org.mockito" %% "mockito-scala" % "2.1.0" % Test,
)

// Assembly
assembly / test := {}
assembly / assemblyJarName := s"${name.value}.jar"

assembly / assemblyOption := (assembly / assemblyOption).value
  .withIncludeScala(false)

assembly / assemblyMergeStrategy := {
  case p if p.endsWith("reference.conf") => MergeStrategy.concat
  case p if p.endsWith("application.conf") => MergeStrategy.concat
  case PathList("META-INF", _@_*) => MergeStrategy.discard
  case PathList("development", _@_*) => MergeStrategy.discard
  case _ => MergeStrategy.first
}