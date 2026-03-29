package parser.auth

import core.auth.Auth

trait AuthParser {
  def name: String
  def parse(value: Any): Auth
}