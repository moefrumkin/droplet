package com.moefrumkin.droplet.interpreter.basicInterpreter.stackframe;

/**
 * A runtime exception thrown by a stack frame
 */
public class StackFrameException extends RuntimeException {

    /**
     * The stack frame where the error occurred
     */
    private final StackFrame stackFrame;
    /**
     * The error message
     */
    private final String message;

    /**
     * Create a new exception with the given stack frame and message
     * @param stackframe the stack frame that caused the exception
     * @param message the error message
     */
    public StackFrameException(StackFrame stackframe, String message) {
        this.stackFrame = stackframe;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message + " " + stackFrame;
    }
}
