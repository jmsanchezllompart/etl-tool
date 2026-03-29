import io.circe.yaml
import parser.pipeline.DataPipelineParser


object Main {
  def main(args: Array[String]): Unit = {
    val json = yaml.parser.parse(
    """
      Metadata:
        DisplayName: "Postgres To BigQuery Pipeline"
        Description: "Sample pipeline that reads data from a PostgreSQL database and writes it to BigQuery"
        Id: "pg2bq_1"
      DataSource:
        PostgresSqlSource:
          Host: "localhost"
          Port: 5432
          Database: "Postgres"
          Auth:
            BasicAuth:
              User: "admin"
              Password: "root"
          RawQuery: "SELECT user_id, user_email, order_id, order_date, item_price, item_id, item_quantity FROM order_items;"
      Transformations:
        - RenameColumn:
            OldColumnName: "user_id"
            NewColumnName: "user"
        - RenameColumn:
            OldColumnName: "user_email"
            NewColumnName: "email"
      DataSink:
        BigQuerySink:
          Project: "test-project"
          Dataset: "test-dataset"
          Table: "test-table"
          Auth:
            ServiceAccountAuth:
              ServiceAccountFile: "opt/shared/service_accounts/testing_service_accounts.json"
          AppendMode:
            Overwrite
    """)
    val scalaJson = json match {
      case Right(json) => json
      case Left(_) => throw new Exception
    }

    val root = scalaJson.hcursor

    val pipeline = DataPipelineParser.parse(root)
  }
}

