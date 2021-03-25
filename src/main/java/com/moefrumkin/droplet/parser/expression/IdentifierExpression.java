package com.moefrumkin.droplet.parser.expression;


import com.moefrumkin.droplet.interpreter.Interpreter;
import com.moefrumkin.droplet.token.Token;

/**
 * An expression that represents the use of an identifier
 */
public record IdentifierExpression(Token identifier) implements Expression {

	/**
	 * Gets the identifier
	 * @return the identifier
	 */
	public Token getIdentifier() { return identifier; }

	@Override
	public int evaluate(Interpreter interpreter){
		return interpreter.interpret(this);
	}
	
	@Override
	public String toString() {
		return identifier.getData();
	}

}
