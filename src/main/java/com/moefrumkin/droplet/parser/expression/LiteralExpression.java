package com.moefrumkin.droplet.parser.expression;

import com.moefrumkin.droplet.interpreter.Interpreter;
import com.moefrumkin.droplet.token.Token;

public record LiteralExpression(Token literal) implements Expression {

	@Override
	public int evaluate(Interpreter interpreter){
		return interpreter.interpret(this);
	}
	
	@Override
	public String toString() {
		return literal.getData();
	}

}
