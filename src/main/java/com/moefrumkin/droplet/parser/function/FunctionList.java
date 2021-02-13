package com.moefrumkin.droplet.parser.function;

import com.moefrumkin.droplet.interpreter.Interpreter;
import com.moefrumkin.droplet.parser.SyntaxTree;

import java.util.List;

/**
 * Class that represents a list of functions
 */
public record FunctionList(List<Function> functions) implements SyntaxTree {

    public void interpret(Interpreter interpreter) {
        interpreter.interpret(this);
    }

    @Override
    public String toString() {
        return functions.toString();
    }

}
