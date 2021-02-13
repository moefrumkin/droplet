package com.moefrumkin.droplet.parser.expression;

import com.moefrumkin.droplet.interpreter.Interpreter;
import com.moefrumkin.droplet.token.Token;

import java.util.List;


public record FunctionCallExpression(Token identifier, List<Expression> arguments) implements Expression {

	@Override
	public int evaluate(Interpreter interpreter){
		return interpreter.interpret(this);
	}
	
	@Override
	public String toString() {
		return "( " + identifier.getData() + " " + arguments + ")";
	}

}
