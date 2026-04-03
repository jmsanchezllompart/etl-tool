package parser.auth

import core.auth.ServiceAccountAuth
import io.circe.parser.parse
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ServiceAccountAuthParserSpec extends AnyFlatSpec with Matchers {
  "ServiceAccountAuthParser" should "parse valid JSON into ServiceAccountAuth" in {
    val json =
      """
        |{
        |  "ServiceAccountFile": "/path/to/file.json"
        |}
        |""".stripMargin

    val cursor = parse(json).toOption.get.hcursor

    val result = ServiceAccountAuthParser.parse(cursor)

    result shouldBe ServiceAccountAuth(
      serviceAccountFile = "/path/to/file.json"
    )
  }

  it should "throw error when ServiceAccountFile is missing" in {
    val json =
      """
        |{
        |  "ServiceAccountFilePath": "/path/to/file.json"
        |}
        |""".stripMargin

    val cursor = parse(json).toOption.get.hcursor

    val ex = intercept[IllegalArgumentException] {
      ServiceAccountAuthParser.parse(cursor)
    }

    ex.getMessage should include("ServiceAccountFile")
  }
}
