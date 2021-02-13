package com.moefrumkin.droplet.parser.statement;


import com.moefrumkin.droplet.interpreter.Interpreter;
import com.moefrumkin.droplet.parser.expression.Expression;
import com.moefrumkin.droplet.parser.Parser;
import com.moefrumkin.droplet.parser.exception.UnexpectedTokenException;
import com.moefrumkin.droplet.parser.exception.UnexpectedTokenTypeException;
import com.moefrumkin.droplet.token.Type;

/**
 * An Expression Statement takes the form Expression;
 */
public record ExpressionStatement(Expression expression) implements Statement {
	
	public static ExpressionStatement parse(Parser parser) throws UnexpectedTokenException, UnexpectedTokenTypeException {
		//parse an expression
		Expression expression = Expression.parse(parser);
		
		//expect a semicolon
		parser.match(Type.SPECIAL, ";");
		
		return new ExpressionStatement(expression);
	}

	@Override
	public void interpret(Interpreter interpreter){
		interpreter.interpret(this);
	}
	
	@Override
	public String toString() {
		return expression.toString();
	}

}
