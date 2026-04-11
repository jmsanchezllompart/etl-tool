package parser.sink

import core.auth.ServiceAccountAuth
import core.sink.{Append, BigQuerySink, Merge, Overwrite}
import io.circe.parser.parse
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class BigQuerySinkParserSpec extends AnyFlatSpec with Matchers {
  "BigQuerySinkParser" should "parse valid JSON into BigQuerySink when AppendMode is Overwrite" in {
    val json =
      """
        |{
        |  "Project": "test-project",
        |  "Dataset": "test-dataset",
        |  "Table": "test-table",
        |  "Auth": {
        |    "ServiceAccountAuth": {
        |      "ServiceAccountFile": "/path/to/file.json"
        |    }
        |  },
        |  "AppendMode": {
        |    "Overwrite": {}
        |  }
        |}
        |""".stripMargin

    val cursor = parse(json).toOption.get.hcursor

    val result = BigQuerySinkParser.parse(cursor)

    result shouldBe BigQuerySink(
      project = "test-project",
      dataset = "test-dataset",
      table = "test-table",
      auth = ServiceAccountAuth(
        serviceAccountFile = "/path/to/file.json"
      ),
      appendMode = Overwrite()
    )
  }

  it should "parse valid JSON into BigQuerySink when AppendMode is Append" in {
    val json =
      """
        |{
        |  "Project": "test-project",
        |  "Dataset": "test-dataset",
        |  "Table": "test-table",
        |  "Auth": {
        |    "ServiceAccountAuth": {
        |      "ServiceAccountFile": "/path/to/file.json"
        |    }
        |  },
        |  "AppendMode": {
        |    "Append": {}
        |  }
        |}
        |""".stripMargin

    val cursor = parse(json).toOption.get.hcursor

    val result = BigQuerySinkParser.parse(cursor)

    result shouldBe BigQuerySink(
      project = "test-project",
      dataset = "test-dataset",
      table = "test-table",
      auth = ServiceAccountAuth(
        serviceAccountFile = "/path/to/file.json"
      ),
      appendMode = Append()
    )
  }

  it should "parse valid JSON into BigQuerySink when AppendMode is Merge" in {
    val json =
      """
        |{
        |  "Project": "test-project",
        |  "Dataset": "test-dataset",
        |  "Table": "test-table",
        |  "Auth": {
        |    "ServiceAccountAuth": {
        |      "ServiceAccountFile": "/path/to/file.json"
        |    }
        |  },
        |  "AppendMode": {
        |    "Merge": {
        |       "IdFields": [
        |         "user_id"
        |       ]
        |    }
        |  }
        |}
        |""".stripMargin

    val cursor = parse(json).toOption.get.hcursor

    val result = BigQuerySinkParser.parse(cursor)

    result shouldBe BigQuerySink(
      project = "test-project",
      dataset = "test-dataset",
      table = "test-table",
      auth = ServiceAccountAuth(
        serviceAccountFile = "/path/to/file.json"
      ),
      appendMode = Merge(
        idFields = List("user_id")
      )
    )
  }

  it should "throw error when Project is missing" in {
    val json =
      """
        |{
        |  "Dataset": "test-dataset",
        |  "Table": "test-table",
        |  "Auth": {
        |    "ServiceAccountAuth": {
        |      "ServiceAccountFile": "/path/to/file.json"
        |    }
        |  },
        |  "AppendMode": {
        |    "Append": {}
        |  }
        |}
        |""".stripMargin

    val cursor = parse(json).toOption.get.hcursor

    val ex = intercept[IllegalArgumentException] {
      BigQuerySinkParser.parse(cursor)
    }

    ex.getMessage should include("Project")
  }

  it should "throw error when Dataset is missing" in {
    val json =
      """
        |{
        |  "Project": "test-project",
        |  "Table": "test-table",
        |  "Auth": {
        |    "ServiceAccountAuth": {
        |      "ServiceAccountFile": "/path/to/file.json"
        |    }
        |  },
        |  "AppendMode": {
        |    "Append": {}
        |  }
        |}
        |""".stripMargin

    val cursor = parse(json).toOption.get.hcursor

    val ex = intercept[IllegalArgumentException] {
      BigQuerySinkParser.parse(cursor)
    }

    ex.getMessage should include("Dataset")
  }

  it should "throw error when Table is missing" in {
    val json =
      """
        |{
        |  "Project": "test-project",
        |  "Dataset": "test-dataset",
        |  "Auth": {
        |    "ServiceAccountAuth": {
        |      "ServiceAccountFile": "/path/to/file.json"
        |    }
        |  },
        |  "AppendMode": {
        |    "Append": {}
        |  }
        |}
        |""".stripMargin

    val cursor = parse(json).toOption.get.hcursor

    val ex = intercept[IllegalArgumentException] {
      BigQuerySinkParser.parse(cursor)
    }

    ex.getMessage should include("Table")
  }

  it should "throw error when Auth is missing" in {
    val json =
      """
        |{
        |  "Project": "test-project",
        |  "Dataset": "test-dataset",
        |  "Table": "test-table",
        |  "AppendMode": {
        |    "Append": {}
        |  }
        |}
        |""".stripMargin

    val cursor = parse(json).toOption.get.hcursor

    val ex = intercept[IllegalArgumentException] {
      BigQuerySinkParser.parse(cursor)
    }

    ex.getMessage should include("Auth")
  }

  it should "throw error when AppendMode is missing" in {
    val json =
      """
        |{
        |  "Project": "test-project",
        |  "Dataset": "test-dataset",
        |  "Table": "test-table",
        |  "Auth": {
        |    "ServiceAccountAuth": {
        |      "ServiceAccountFile": "/path/to/file.json"
        |    }
        |  }
        |}
        |""".stripMargin

    val cursor = parse(json).toOption.get.hcursor

    val ex = intercept[IllegalArgumentException] {
      BigQuerySinkParser.parse(cursor)
    }

    ex.getMessage should include("AppendMode")
  }
}
