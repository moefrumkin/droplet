package com.moefrumkin.droplet.parser.expression;

import com.moefrumkin.droplet.interpreter.Interpreter;

import java.util.Map;

/**
 * A binary operation expression has the form UnaryOperation Expression
 */
public record UnaryOperationExpression(Type type, Expression operand) implements Expression {
	
	static final Map<String, Type> DEFAULT_TYPE_MAP = Map.ofEntries(
				Map.entry("-", Type.NEGATION),
				Map.entry("!", Type.BOOLEAN_NEGATION)
			);
	
	public enum Type {
		NEGATION,
		BOOLEAN_NEGATION
    }
	
	//convenience method to convert from String to op type
	public static Type typeFromString(String string) {
		return DEFAULT_TYPE_MAP.get(string);
	}

	@Override
	public int evaluate(Interpreter interpreter){
		return interpreter.interpret(this);
	}

	@Override
	public String toString() {
		return "( " + type + " " + operand + " )";
	}

}
