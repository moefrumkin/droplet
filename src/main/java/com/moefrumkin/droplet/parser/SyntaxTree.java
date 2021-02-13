package com.moefrumkin.droplet.parser;

import com.moefrumkin.droplet.interpreter.Interpreter;

/**
 * This interface represents an abstract syntax tree in the droplet language
 */
public interface SyntaxTree {
    /**
     * Accept and {@link Interpreter} visitor object
     * @param interpreter The {@link Interpreter}
     */
    void interpret(Interpreter interpreter);
}
