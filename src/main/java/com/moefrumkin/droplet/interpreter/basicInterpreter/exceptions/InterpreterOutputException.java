package com.moefrumkin.droplet.interpreter.basicInterpreter.exceptions;

import com.moefrumkin.droplet.interpreter.basicInterpreter.stackframe.StackFrame;

import java.io.IOException;

/**
 * An exception thrown if an IO exception occurs during the execution of a droplet program
 */
public class InterpreterOutputException extends RuntimeException {
    /**
     * The error message
     */
    private final String message;
    /**
     * The underlying exception
     */
    private final IOException exception;
    /**
     * The frame where the error occurred
     */
    private final StackFrame frame;

    /**
     * Creates a new exception
     * @param message the error message
     * @param exception the underlying exception
     * @param frame the stack frame where the error occurred
     */
    public InterpreterOutputException(String message, IOException exception, StackFrame frame) {
        this.message = message;
        this.exception = exception;
        this.frame = frame;
    }

    @Override
    public String getMessage() {
        return message + " " + exception.getMessage() + " at " + frame;
    }
}
