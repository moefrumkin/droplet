package com.moefrumkin.droplet.interpreter.basicInterpreter.exceptions;


import com.moefrumkin.droplet.parser.SyntaxTree;

/**
 * An exception thrown if an error occurs during the evaluation of a Droplet Program
 */
public class InterpreterRuntimeException extends RuntimeException {
    private final String message;
    private final SyntaxTree tree;

    public InterpreterRuntimeException(String message, SyntaxTree tree) {
        this.message = message;
        this.tree = tree;
    }

    @Override
    public String getMessage() {
        return message + " at " + tree;
    }
}
