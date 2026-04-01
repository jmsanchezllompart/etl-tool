package core.auth

/**
 * A trait representing authentication-related data.
 *
 * This trait does not define behavior itself, but provides
 * a common structure for storing authentication information.
 * It can be mixed into various classes, each of which may
 * use this data according to its own interface and logic.
 *
 * Examples of data that might be included:
 *   - user ID or username
 *   - roles or permissions
 *   - authentication tokens
 *
 * By keeping authentication data separate from behavior, this
 * trait allows flexibility and reuse across different components.
 */
trait Auth
