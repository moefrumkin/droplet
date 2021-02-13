package com.moefrumkin.droplet.interpreter.basicInterpreter.stackframe;

import com.moefrumkin.droplet.parser.function.Function;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A top level stack frame. This contains all the variables in the global scope.
 */
public class GlobalStackFrame implements StackFrame {

    private final Map<String, Integer> variableMap;
    private final Map<String, Function> functionMap;
    private boolean toReturn;
    private int exitCode;

    public GlobalStackFrame() {
        variableMap = new HashMap<>();
        functionMap = new HashMap<>();
        toReturn = false;
    }

    public GlobalStackFrame(int defaultExitCode) {
        this();
        exitCode = defaultExitCode;
    }

    @Override
    public void addFunction(Function function) {
        if (functionMap.containsKey(function.name().getData()))
            throw new StackFrameException(this, "The function " + function.name().getData() + " is already defined");

        functionMap.put(function.name().getData(), function);
    }

    @Override
    public StackFrame getSuper() {
        return this;
    }

    @Override
    public Function getFunction(String name) {
        return Optional.ofNullable(functionMap.get(name)).orElseThrow(() -> new StackFrameException(this, "The function " + name + " is not defined"));
    }

    @Override
    public void addVariable(String name, int value) {
        if (variableMap.containsKey(name))
            throw new StackFrameException(this, "The variable " + name + " has already been initialized");

        variableMap.put(name, value);
    }

    @Override
    public void setVariable(String name, int value) {
        if (!variableMap.containsKey(name))
            throw new StackFrameException(this, "The variable " + name + " has not been initialized");

        variableMap.put(name, value);
    }

    @Override
    public int getVariable(String name) {
        if (!variableMap.containsKey(name))
            throw new StackFrameException(this, "The variable " + name + " has not been initialized");

        return variableMap.get(name);
    }

    //no-op
    @Override
    public void returnFunction(int value) {
        exitCode = value;
        toReturn = true;
    }

    @Override
    public void returnFunction() {
        toReturn = true;
    }

    @Override
    public int getReturnValue() {
        return exitCode;
    }

    @Override
    public boolean getToReturn() {
        return toReturn;
    }

    @Override
    public int hashCode() {
        return variableMap.hashCode() * functionMap.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof GlobalStackFrame other)
            return variableMap.equals(other.variableMap)
                    && functionMap.equals(other.functionMap)
                    && toReturn == other.toReturn
                    && exitCode == other.exitCode;
        else
            return false;
    }
}
