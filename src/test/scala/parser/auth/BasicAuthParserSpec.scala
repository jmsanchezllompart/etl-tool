package parser.auth

import core.auth.BasicAuth
import io.circe.parser.parse
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class BasicAuthParserSpec extends AnyFlatSpec with Matchers {
  "BasicAuthParser" should "parse valid JSON into BasicAuth" in {
    val json =
      """
        |{
        |  "User": "user",
        |  "Password": "pass"
        |}
        |""".stripMargin

    val cursor = parse(json).toOption.get.hcursor

    val result = BasicAuthParser.parse(cursor)

    result shouldBe BasicAuth(
      user = "user",
      password = "pass"
    )
  }

  it should "throw error when User is missing" in {
    val json =
      """
        |{
        |  "Password": "pass"
        |}
        |""".stripMargin

    val cursor = parse(json).toOption.get.hcursor

    val ex = intercept[IllegalArgumentException] {
      BasicAuthParser.parse(cursor)
    }

    ex.getMessage should include("User")
  }

  it should "throw error when Password is missing" in {
    val json =
      """
        |{
        |  "User": "user"
        |}
        |""".stripMargin

    val cursor = parse(json).toOption.get.hcursor

    val ex = intercept[IllegalArgumentException] {
      BasicAuthParser.parse(cursor)
    }

    ex.getMessage should include("Password")
  }
}
