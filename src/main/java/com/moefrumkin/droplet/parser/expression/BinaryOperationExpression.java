package com.moefrumkin.droplet.parser.expression;

import com.moefrumkin.droplet.interpreter.Interpreter;

import java.util.Map;

/**
 * A Binary Operation Expression has the form Expression BinaryOperation Expression
 */
@SuppressWarnings("SpellCheckingInspection")
public record BinaryOperationExpression(Type type, Expression left, Expression right) implements Expression {

    static final Map<String, Type> DEFAULT_TYPE_MAP = Map.ofEntries(
            Map.entry("=", Type.ASSIGNMENT),
            Map.entry("+", Type.ADDITION),
            Map.entry("-", Type.SUBTRACTION),
            Map.entry("*", Type.MULTIPLICATION),
            Map.entry("||", Type.OR),
            Map.entry("&&", Type.AND),
            Map.entry("==", Type.EQUALS),
            Map.entry("!=", Type.NOTEQUALS),
            Map.entry("<", Type.LESS),
            Map.entry(">", Type.GREATER)
    );

    public enum Type {
        ASSIGNMENT,
        ADDITION,
        SUBTRACTION,
        MULTIPLICATION,
        OR,
        AND,
        EQUALS,
        @SuppressWarnings("SpellCheckingInspection") NOTEQUALS,
        LESS,
        GREATER
    }

    @Override
    public int evaluate(Interpreter interpreter) {
        return interpreter.interpret(this);
    }

    /**
     * Returns an operation type that the given string represents
     *
     * @param string the operation
     * @return the Type
     */
    public static Type typeFromString(String string) {
        return DEFAULT_TYPE_MAP.get(string);
    }

    @Override
    public String toString() {
        return "( " + type + " " + left + " " + right + " )";
    }

}
