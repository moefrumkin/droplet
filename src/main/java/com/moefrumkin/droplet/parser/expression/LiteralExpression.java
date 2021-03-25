package com.moefrumkin.droplet.parser.expression;

import com.moefrumkin.droplet.interpreter.Interpreter;
import com.moefrumkin.droplet.token.Token;
import com.moefrumkin.droplet.token.Type;

/**
 * Represents a numeric literal
 */
public record LiteralExpression(Token literal) implements Expression {

	/**
	 * Static factory method to create a literal representing an integer
	 * @param i the integer to represent
	 * @return the literal representing the integer
	 */
	public static LiteralExpression from(int i) { return new LiteralExpression(new Token(Type.LITERAL, String.valueOf(i))); }

	@Override
	public int evaluate(Interpreter interpreter){
		return interpreter.interpret(this);
	}
	
	@Override
	public String toString() {
		return literal.getData();
	}

}
