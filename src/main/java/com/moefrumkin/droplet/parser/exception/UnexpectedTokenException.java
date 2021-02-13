package com.moefrumkin.droplet.parser.exception;

import com.moefrumkin.droplet.token.Token;
import com.moefrumkin.droplet.token.Type;

/**
 * An exception thrown when the parser has seen a token that does not match the type or data expected
 */
public class UnexpectedTokenException extends Exception {
    private final Token given;
    private final Type expectedType;
    private final String expectedData;

    public UnexpectedTokenException(Token given, Type expectedType, String expectedData) {
        this.given = given ;
        this.expectedType = expectedType;
        this.expectedData = expectedData;
    }

    public Token getGiven() { return given; }
    public Type getExpectedType() { return expectedType; }
    public String getExpectedData() { return expectedData; }

    @Override
    public String getMessage() {
        return "Expected token of type " + expectedType + " with data " + expectedData + ", given " + given;
    }
}
