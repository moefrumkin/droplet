package com.moefrumkin.droplet.interpreter.basicInterpreter.stackframe;

import com.moefrumkin.droplet.parser.function.Function;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * This class represents a simple <a href="https://en.wikipedia.org/wiki/Call_stack">call stack</a>
 */
public class SimpleStackFrame implements StackFrame {

    protected final Map<String, Integer> variableMap;
    protected final StackFrame superFrame;

    /**
     * Create a stack frame with the given parent frame
     *
     * @param superFrame the parent frame
     */
    public SimpleStackFrame(StackFrame superFrame) {
        variableMap = new HashMap<>();

        this.superFrame = superFrame;
    }

    @Override
    public void addFunction(Function function) {
        throw new StackFrameException(this, "All functions must be defined in the global scope");
    }

    /**
     * @return The parent stack frame
     */
    @Override
    public StackFrame getSuper() {
        return superFrame;
    }

    /**
     * @param name the name of the function to return
     * @return the  {@link Function} object with the given name
     */
    @Override
    public Function getFunction(String name) {
        return this.superFrame.getFunction(name);
    }

    /**
     * adds a variable to the stack frame and initializes it to the given value
     *
     * @param name  The name of the variable
     * @param value The initial value
     */
    @Override
    public void addVariable(String name, int value) {
        if (variableMap.containsKey(name))
            throw new StackFrameException(this, "The variable " + name + " has already been initialized.");

        variableMap.put(name, value);
    }

    /**
     * Sets a variable to a given value
     *
     * @param name  The variable
     * @param value The value
     */
    @Override
    public void setVariable(String name, int value) {
        if (variableMap.containsKey(name))
            variableMap.put(name, value);
        else
            superFrame.setVariable(name, value);
    }

    /**
     * Gets the value of a variable
     *
     * @param name the name of the variable
     * @return The value of the variable
     */
    @Override
    public int getVariable(String name) {
        if (variableMap.containsKey(name))
            return variableMap.get(name);
        else
            return superFrame.getVariable(name);
    }

    /**
     * Mark the most immediate function frame to return with the given value
     *
     * @param value the return value
     */
    @Override
    public void returnFunction(int value) {
        superFrame.returnFunction(value);
    }

    /**
     * Mark the most immediate function frame to return
     */
    @Override
    public void returnFunction() {
        superFrame.returnFunction();
    }

    /**
     * Gets the return value for the nearest function frame
     * @return the return value
     */
    @Override
    public int getReturnValue(){
        return superFrame.getReturnValue();
    }

    /**
     * Returns whether the frame is contained by a frame that has been marked to be returned
     *
     * @return true if the frame is marked for return
     */
    @Override
    public boolean getToReturn() {
        return superFrame.getToReturn();
    }

    @Override
    public int hashCode() {
        return variableMap.hashCode() * superFrame.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof SimpleStackFrame other) {
            return other.variableMap.equals(variableMap)
                    && other.superFrame.equals(superFrame);
        }
        return false;
    }

    @Override
    public String toString() {
        return "Stack Frame in " + superFrame;

    }

}
