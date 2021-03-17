package com.moefrumkin.droplet.parser.expression;

import com.moefrumkin.droplet.interpreter.Interpreter;

/**
 * An enclosed expression takes the form ( Expression )
 */
public record EnclosedExpression(Expression expression) implements Expression {
    @Override
    public int evaluate(Interpreter interpreter){
        return interpreter.interpret(this);
    }

    @Override
    public String toString() { return  "(" + expression + ")"; }
}
