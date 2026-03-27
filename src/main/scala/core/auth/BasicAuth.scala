package core.auth

case class BasicAuth(user: String, password: String) extends Auth
