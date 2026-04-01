package core.auth

/**
 * Represents authentication using a service account file.
 *
 * This class is a concrete implementation of the [[core.auth.Auth]] trait
 * that stores the path to a service account credentials file.
 * It can be used by components that require service account-based
 * authentication, such as cloud services or APIs.
 *
 * Example usage:
 * {{{
 *   val auth = ServiceAccountAuth("/path/to/serviceAccount.json")
 *   println(auth.serviceAccountFile) // "/path/to/serviceAccount.json"
 * }}}
 *
 * @param serviceAccountFile the file path to the service account credentials
 */
case class ServiceAccountAuth(serviceAccountFile: String) extends Auth
