package com.moefrumkin.droplet.parser.statement;

import com.moefrumkin.droplet.interpreter.Interpreter;
import com.moefrumkin.droplet.parser.expression.Expression;
import com.moefrumkin.droplet.parser.Parser;
import com.moefrumkin.droplet.parser.exception.UnexpectedTokenException;
import com.moefrumkin.droplet.parser.exception.UnexpectedTokenTypeException;
import com.moefrumkin.droplet.token.Type;

/**
 * A return statement takes the form return Expression;
 */
public record ReturnStatement(Expression expression) implements Statement {

	/**
	 * Parses a return statement
	 * @param parser the parser
	 * @return the statement
	 * @throws UnexpectedTokenException if an unexpected token is seen
	 * @throws UnexpectedTokenTypeException if an unexpected token type is seen
	 */
	public static ReturnStatement parse(Parser parser) throws UnexpectedTokenException, UnexpectedTokenTypeException {
		//match return keyword
		parser.match(Type.KEYWORD, "return");
		
		//parse expression
		Expression expression = Expression.parse(parser);
		
		//expect semicolon
		parser.match(Type.SPECIAL, ";");

		return new ReturnStatement(expression);
	}

	@Override
	public void interpret(Interpreter interpreter) {
		interpreter.interpret(this);
	}
	
	@Override
	public String toString() {
		return "( return " + expression + " )";
	}

}
