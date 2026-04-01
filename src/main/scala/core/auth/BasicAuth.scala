package core.auth

/**
 * Represents basic authentication credentials.
 *
 * This class is a concrete implementation of the [[core.auth.Auth]] trait
 * containing a username and password. It can be used by any
 * component that requires authentication data in a simple form.
 *
 * Example usage:
 * {{{
 *   val auth = BasicAuth("alice", "secret123")
 *   println(auth.user)       // "alice"
 *   println(auth.password)   // "secret123"
 * }}}
 *
 * @param user the username for authentication
 * @param password the password for authentication
 */
case class BasicAuth(user: String, password: String) extends Auth
