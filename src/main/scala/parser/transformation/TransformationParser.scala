package parser.transformation

import core.transformation.Transformation

trait TransformationParser {
  def name: String
  def parse(value: Any): Transformation
}
