package com.github.cargoclean.infrastructure.adapter.security;

/**
 * This exception will be thrown by {@link CargoSecurityAdapter} if
 * authentication is required and the user is not authenticated.
 */
class AuthenticationRequiredException extends Exception {
}
