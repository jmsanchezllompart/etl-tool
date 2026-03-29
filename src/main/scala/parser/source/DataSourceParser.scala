package parser.source

import core.source.DataSource

trait DataSourceParser {
  def name: String
  def parse(value: Any): DataSource
}

