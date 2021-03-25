package com.moefrumkin.droplet.interpreter.basicInterpreter.stackframe;

import com.moefrumkin.droplet.parser.function.Function;

import java.util.Optional;

/**
 * This interface represents a <a href="https://en.wikipedia.org/wiki/Call_stack">call stack</a>
 */
public interface StackFrame {

    /**
     * adds a function to the stack frame
     * @param function the function to add
     */
    void addFunction(Function function);

    /**
     * @return The parent stack frame
     */
    StackFrame getSuper();

    /**
     * @param name the name of the function to return
     * @return the  {@link Function} object with the given name
     */
    Function getFunction(String name);

    /**
     * adds a variable to the stack frame and initializes it to the given value
     *
     * @param name  The name of the variable
     * @param value The initial value
     */
    void addVariable(String name, int value);

    /**
     * Sets a variable to a given value
     *
     * @param name  The variable
     * @param value The value
     */
    void setVariable(String name, int value);

    /**
     * Gets the value of a variable
     *
     * @param name the name of the variable
     * @return The value of the variable
     */
    int getVariable(String name);

    /**
     * Mark the most immediate function frame to return with the given value
     *
     * @param value the return value
     */
    void returnFunction(int value);

    /**
     * Mark the most immediate function frame to return
     */
    void returnFunction();

    /**
     * Gets the return value for the nearest function frame
     * @return the return value
     */
    int getReturnValue();

    /**
     * Returns whether the frame is contained by a frame that has been marked to be returned
     *
     * @return whether the frame is marked for return
     */
    boolean getToReturn();
}
