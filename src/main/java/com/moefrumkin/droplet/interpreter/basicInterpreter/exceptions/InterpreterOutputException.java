package com.moefrumkin.droplet.interpreter.basicInterpreter.exceptions;

import com.moefrumkin.droplet.interpreter.basicInterpreter.stackframe.StackFrame;

import java.io.IOException;

/**
 * An exception thrown if an IO exception occurs during the execution of a droplet program
 */
public class InterpreterOutputException extends RuntimeException {
    private final String message;
    private final IOException exception;
    private final StackFrame frame;

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
