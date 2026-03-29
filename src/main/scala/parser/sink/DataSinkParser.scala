package parser.sink

import core.sink.DataSink

trait DataSinkParser {
  def name: String
  def parse(value: Any): DataSink
}
