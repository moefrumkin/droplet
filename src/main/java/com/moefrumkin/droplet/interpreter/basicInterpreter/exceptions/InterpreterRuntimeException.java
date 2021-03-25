package com.moefrumkin.droplet.interpreter.basicInterpreter.exceptions;


import com.moefrumkin.droplet.parser.SyntaxTree;

/**
 * An exception thrown if an error occurs during the evaluation of a Droplet Program
 */
public class InterpreterRuntimeException extends RuntimeException {
    /**
     * The error message
     */
    private final String message;
    /**
     * The part of the syntax tree the caused the error
     */
    private final SyntaxTree tree;

    /**
     * Creates a new exception
     * @param message the error message
     * @param tree the part of the syntax tree that caused the error
     */
    public InterpreterRuntimeException(String message, SyntaxTree tree) {
        this.message = message;
        this.tree = tree;
    }

    @Override
    public String getMessage() {
        return message + " at " + tree;
    }
}
