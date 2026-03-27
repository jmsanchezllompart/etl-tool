ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.18"

lazy val root = (project in file("."))
  .settings(
    name := "etl-tool"
  )

val sparkVersion = "4.1.1"

libraryDependencies ++= Seq(
  // YAML Parsing
  "org.yaml" % "snakeyaml" % "2.6",
  // Spark
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-mllib" % sparkVersion,
  // PostgresSql
  "org.postgresql" % "postgresql" % "42.7.10"
)