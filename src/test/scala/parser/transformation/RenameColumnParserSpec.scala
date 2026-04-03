package parser.transformation

import core.transformation.RenameColumn
import io.circe.parser.parse
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class RenameColumnParserSpec extends AnyFlatSpec with Matchers {
  "RenameColumnParser" should "parse valid JSON into RenameColumn" in {
    val json =
      """
        |{
        |  "OldColumnName": "user_id",
        |  "NewColumnName": "user"
        |}
        |""".stripMargin

    val cursor = parse(json).toOption.get.hcursor

    val result = RenameColumnParser.parse(cursor)

    result shouldBe RenameColumn(
      oldName = "user_id",
      newName = "user"
    )
  }

  it should "throw error when OldColumnName is missing" in {
    val json =
      """
        |{
        |  "NewColumnName": "user"
        |}
        |""".stripMargin

    val cursor = parse(json).toOption.get.hcursor

    val ex = intercept[IllegalArgumentException] {
      RenameColumnParser.parse(cursor)
    }

    ex.getMessage should include("OldColumnName")
  }

  it should "throw error when NewColumnName is missing" in {
    val json =
      """
        |{
        |  "OldColumnName": "user_id"
        |}
        |""".stripMargin

    val cursor = parse(json).toOption.get.hcursor

    val ex = intercept[IllegalArgumentException] {
      RenameColumnParser.parse(cursor)
    }

    ex.getMessage should include("NewColumnName")
  }
}
