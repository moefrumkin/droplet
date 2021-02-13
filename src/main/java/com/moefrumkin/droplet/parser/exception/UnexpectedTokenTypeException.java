package com.moefrumkin.droplet.parser.exception;

import com.moefrumkin.droplet.token.Token;
import com.moefrumkin.droplet.token.Type;

/**
 * This exception is thrown when the parser expected one type of token be received a different one
 */
public class UnexpectedTokenTypeException extends Exception {

    private final Token given;
    private final Type expectedType;

    public UnexpectedTokenTypeException(Token given, Type expectedType) {
        this.given = given;
        this.expectedType = expectedType;
    }

    public Token getGiven() { return given; }
    public Type getExpectedType() { return expectedType; }

    @Override
    public String getMessage() {
        return "Expected token of type " + expectedType + ", given " + given;
    }
}
