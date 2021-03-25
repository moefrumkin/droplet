package com.moefrumkin.droplet.parser.exception;

import com.moefrumkin.droplet.token.Token;
import com.moefrumkin.droplet.token.Type;

/**
 * This exception is thrown when the parser expected one type of token be received a different one
 */
public class UnexpectedTokenTypeException extends Exception {

    /**
     * The given token
     */
    private final Token given;
    /**
     * The expected type
     */
    private final Type expectedType;

    /**
     * Creates an exception with the given token and type
     * @param given the token that caused the exception
     * @param expectedType the expected type of the token
     */
    public UnexpectedTokenTypeException(Token given, Type expectedType) {
        this.given = given;
        this.expectedType = expectedType;
    }

    /**
     * Gets the token that caused the exception
     * @return the token
     */
    public Token getGiven() { return given; }

    /**
     * Gets the expected type of the token
     * @return the type
     */
    public Type getExpectedType() { return expectedType; }

    @Override
    public String getMessage() {
        return "Expected token of type " + expectedType + ", given " + given;
    }
}
