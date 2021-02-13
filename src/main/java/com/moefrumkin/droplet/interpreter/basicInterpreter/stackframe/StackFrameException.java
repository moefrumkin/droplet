package com.moefrumkin.droplet.interpreter.basicInterpreter.stackframe;

public class StackFrameException extends RuntimeException {
    private final StackFrame stackFrame;
    private final String message;

    public StackFrameException(StackFrame stackframe, String message) {
        this.stackFrame = stackframe;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message + " " + stackFrame;
    }
}
