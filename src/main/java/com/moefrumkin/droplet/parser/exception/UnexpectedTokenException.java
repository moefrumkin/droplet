package com.moefrumkin.droplet.parser.exception;

import com.moefrumkin.droplet.token.Token;
import com.moefrumkin.droplet.token.Type;

/**
 * An exception thrown when the parser has seen a token that does not match the type or data expected
 */
public class UnexpectedTokenException extends Exception {

    /**
     * The given token
     */
    private final Token given;
    /**
     * The expected type
     */
    private final Type expectedType;
    /**
     * The expected data
     */
    private final String expectedData;

    /**
     * Creates a new exception with the given token, type and data
     * @param given the token that caused the exception
     * @param expectedType the expected type of the token
     * @param expectedData the expected data of the token
     */
    public UnexpectedTokenException(Token given, Type expectedType, String expectedData) {
        this.given = given ;
        this.expectedType = expectedType;
        this.expectedData = expectedData;
    }

    /**
     * Gets the token the caused the error
     * @return the token
     */
    public Token getGiven() { return given; }

    /**
     * Gets the expected type of the token
     * @return the type
     */
    public Type getExpectedType() { return expectedType; }

    /**
     * Gets the expected data of the token
     * @return the data
     */
    public String getExpectedData() { return expectedData; }

    @Override
    public String getMessage() {
        return "Expected token of type " + expectedType + " with data " + expectedData + ", given " + given;
    }
}
