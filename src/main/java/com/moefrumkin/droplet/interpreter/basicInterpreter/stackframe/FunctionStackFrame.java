package com.moefrumkin.droplet.interpreter.basicInterpreter.stackframe;

import com.moefrumkin.droplet.parser.function.Function;

import java.util.Objects;

/**
 * A class that extends the {@link SimpleStackFrame} class to include a special return value;
 */
public class FunctionStackFrame extends SimpleStackFrame {

    private String name;
    private int returnValue;
    private boolean toReturn;

    /**
     * Creates a function stack frame with the default return value
     */
    public FunctionStackFrame(StackFrame superFrame, int defaultReturn) {
        super(superFrame);
        name = "anonymous";
        returnValue = defaultReturn;
        toReturn = false;
    }

    public FunctionStackFrame(StackFrame superFrame, int defaultReturn, String name) {
        this(superFrame, defaultReturn);
        this.name = name;
    }

    @Override
    public void returnFunction(int value) {
        this.returnValue = value;
        this.toReturn = true;
    }

    @Override
    public void returnFunction() {
        this.toReturn = true;
    }

    @Override
    public int getReturnValue() {
        return returnValue;
    }

    @Override
    public boolean getToReturn() {
        return toReturn;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof FunctionStackFrame other)
            return name.equals(other.name)
                && returnValue == other.returnValue
                && toReturn == other.toReturn
                && superFrame.equals(other.superFrame);
        else
            return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(name);

        builder.append('(');
        builder.append(returnValue);

        if(toReturn){
            builder.append(',');
            builder.append("returned");
        }

        builder.append(')');
        builder.append(" in");
        builder.append(superFrame);

        return builder.toString();
    }
}