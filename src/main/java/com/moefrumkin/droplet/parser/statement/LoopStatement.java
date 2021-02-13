package com.moefrumkin.droplet.parser.statement;


import com.moefrumkin.droplet.interpreter.Interpreter;
import com.moefrumkin.droplet.parser.expression.Expression;
import com.moefrumkin.droplet.parser.Parser;
import com.moefrumkin.droplet.parser.exception.UnexpectedTokenException;
import com.moefrumkin.droplet.parser.exception.UnexpectedTokenTypeException;
import com.moefrumkin.droplet.token.Type;

/**
 * A Loop Statement takes the form while (Expression) Statement
 */
public record LoopStatement(Expression condition, Statement body) implements Statement {

	public static LoopStatement parse(Parser parser) throws UnexpectedTokenException, UnexpectedTokenTypeException {
		//match while keyword
		parser.match(Type.KEYWORD, "while");

		//match open paren
		parser.match(Type.SPECIAL, "(");

		//parse expression
		Expression condition = Expression.parse(parser);

		//match close paren
		parser.match(Type.SPECIAL, ")");

		//parse statement
		Statement body = Statement.parse(parser);

		return new LoopStatement(condition, body);
	}

	@Override
	public void interpret(Interpreter interpreter){
		interpreter.interpret(this);
	}

	@Override
	public String toString() {
		return "( while " + condition + " " + body + " )";
	}

}
