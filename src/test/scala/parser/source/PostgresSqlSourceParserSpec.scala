package parser.source

import core.auth.BasicAuth
import core.source.PostgresSqlSource
import io.circe.parser.parse
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class PostgresSqlSourceParserSpec extends AnyFlatSpec with Matchers {
  "PostgresSqlSourceParser" should "parse valid JSON into PostgresSqlSource" in {
    val json =
      """
        |{
        |  "Host": "localhost",
        |  "Port": 5432,
        |  "Database": "my_db",
        |  "Auth": {
        |    "BasicAuth": {
        |      "User": "user",
        |      "Password": "pass"
        |    }
        |  },
        |  "RawQuery": "SELECT * FROM users"
        |}
        |""".stripMargin

    val cursor = parse(json).toOption.get.hcursor

    val result = PostgresSqlSourceParser.parse(cursor)

    result shouldBe PostgresSqlSource(
      host = "localhost",
      port = "5432",
      database = "my_db",
      auth = BasicAuth("user", "pass"),
      query = "SELECT * FROM users"
    )
  }

  it should "throw error when Host is missing" in {
    val json =
      """
        |{
        |  "Port": 5432,
        |  "Database": "my_db",
        |  "Auth": {
        |    "BasicAuth": {
        |      "User": "user",
        |      "Password": "pass"
        |    }
        |  },
        |  "RawQuery": "SELECT * FROM users"
        |}
        |""".stripMargin

    val cursor = parse(json).toOption.get.hcursor

    val ex = intercept[IllegalArgumentException] {
      PostgresSqlSourceParser.parse(cursor)
    }

    ex.getMessage should include("Host")
  }

  it should "throw error when Port is not an integer" in {
    val json =
      """
        |{
        |  "Host": "localhost",
        |  "Database": "my_db",
        |  "Auth": {
        |    "BasicAuth": {
        |      "User": "user",
        |      "Password": "pass"
        |    }
        |  },
        |  "RawQuery": "SELECT * FROM users"
        |}
        |""".stripMargin

    val cursor = parse(json).toOption.get.hcursor

    val ex = intercept[IllegalArgumentException] {
      PostgresSqlSourceParser.parse(cursor)
    }

    ex.getMessage should include("Port")
  }

  it should "throw error when Database is missing" in {
    val json =
      """
        |{
        |  "Host": "localhost",
        |  "Port": 5432,
        |  "Auth": {
        |    "BasicAuth": {
        |      "User": "user",
        |      "Password": "pass"
        |    }
        |  },
        |  "RawQuery": "SELECT * FROM users"
        |}
        |""".stripMargin

    val cursor = parse(json).toOption.get.hcursor

    val ex = intercept[IllegalArgumentException] {
      PostgresSqlSourceParser.parse(cursor)
    }

    ex.getMessage should include("Database")
  }

  it should "throw error when RawQuery is missing" in {
    val json =
      """
        |{
        |  "Host": "localhost",
        |  "Port": 5432,
        |  "Database": "my_db",
        |  "Auth": {
        |    "BasicAuth": {
        |      "User": "user",
        |      "Password": "pass"
        |    }
        |  }
        |}
        |""".stripMargin

    val cursor = parse(json).toOption.get.hcursor

    val ex = intercept[IllegalArgumentException] {
      PostgresSqlSourceParser.parse(cursor)
    }

    ex.getMessage should include("RawQuery")
  }

  it should "throw error when Auth parsing fails" in {
    val json =
      """
        |{
        |  "Host": "localhost",
        |  "Port": 5432,
        |  "Database": "my_db",
        |  "Auth": "UnknownAuth",
        |  "RawQuery": "SELECT * FROM users"
        |}
        |""".stripMargin

    val cursor = parse(json).toOption.get.hcursor

    val ex = intercept[IllegalArgumentException] {
      PostgresSqlSourceParser.parse(cursor)
    }

    ex.getMessage should include("Auth")
  }
}
