package parser.source


import core.auth.BasicAuth
import core.source.MySqlSource
import io.circe.parser.parse
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MySqlSourceParserSpec extends AnyFlatSpec with Matchers {
  "MySqlSourceParser" should "parse valid JSON into MySqlSource" in {
    val json =
      """
        |{
        |  "Host": "localhost",
        |  "Port": 3306,
        |  "Database": "test_db",
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

    val result = MySqlSourceParser.parse(cursor)

    result shouldBe MySqlSource(
      host = "localhost",
      port = "3306",
      database = "test_db",
      auth = BasicAuth("user", "pass"),
      query = "SELECT * FROM users"
    )
  }

  it should "throw error when Host is missing" in {
    val json =
      """
        |{
        |  "Port": 3306,
        |  "Database": "test_db",
        |  "Auth": {
        |    "BasicAuth": {
        |      "User": "u",
        |      "Password": "p"
        |    }
        |  },
        |  "RawQuery": "SELECT 1"
        |}
        |""".stripMargin

    val cursor = parse(json).toOption.get.hcursor

    val ex = intercept[IllegalArgumentException] {
      MySqlSourceParser.parse(cursor)
    }

    ex.getMessage should include("Host")
  }

  it should "throw error when Port is not an integer" in {
    val json =
      """
        |{
        |  "Host": "localhost",
        |  "Database": "test_db",
        |  "Auth": {
        |    "BasicAuth": {
        |      "User": "u",
        |      "Password": "p"
        |    }
        |  },
        |  "RawQuery": "SELECT 1"
        |}
        |""".stripMargin

    val cursor = parse(json).toOption.get.hcursor

    val ex = intercept[IllegalArgumentException] {
      MySqlSourceParser.parse(cursor)
    }

    ex.getMessage should include("Port")
  }

  it should "throw error when Database is missing" in {
    val json =
      """
        |{
        |  "Host": "localhost",
        |  "Port": 3306,
        |  "Auth": {
        |    "BasicAuth": {
        |      "User": "u",
        |      "Password": "p"
        |    }
        |  },
        |  "RawQuery": "SELECT 1"
        |}
        |""".stripMargin

    val cursor = parse(json).toOption.get.hcursor

    val ex = intercept[IllegalArgumentException] {
      MySqlSourceParser.parse(cursor)
    }

    ex.getMessage should include("Database")
  }

  it should "throw error when RawQuery is missing" in {
    val json =
      """
        |{
        |  "Host": "localhost",
        |  "Port": 3306,
        |  "Database": "test_db",
        |  "Auth": {
        |    "BasicAuth": {
        |      "User": "u",
        |      "Password": "p"
        |    }
        |  }
        |}
        |""".stripMargin

    val cursor = parse(json).toOption.get.hcursor

    val ex = intercept[IllegalArgumentException] {
      MySqlSourceParser.parse(cursor)
    }

    ex.getMessage should include("RawQuery")
  }

  it should "throw error when Auth parsing fails" in {
    val json =
      """
        |{
        |  "Host": "localhost",
        |  "Port": 3306,
        |  "Database": "test_db",
        |  "Auth": "UnknownAuth",
        |  "RawQuery": "SELECT 1"
        |}
        |""".stripMargin

    val cursor = parse(json).toOption.get.hcursor

    val ex = intercept[IllegalArgumentException] {
      MySqlSourceParser.parse(cursor)
    }

    ex.getMessage should include("Auth")
  }
}