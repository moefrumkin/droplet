package com.moefrumkin.droplet.parser.expression;


import com.moefrumkin.droplet.interpreter.Interpreter;
import com.moefrumkin.droplet.token.Token;

public record IdentifierExpression(Token identifier) implements Expression {
	
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
