/**
 * Custom exception class for JWT authentication errors.
 */
package com.csme.csmeapi.fin.config;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {

    /**
     * Serial version UID for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new JwtAuthenticationException with the specified message.
     * @param message The detail message.
     */
    public JwtAuthenticationException(String message) {
        super(message);
    }

    /**
     * Constructs a new JwtAuthenticationException with the specified message and cause.
     * @param message The detail message.
     * @param cause The cause of the exception.
     */
    public JwtAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
