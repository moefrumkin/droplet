package com.moefrumkin.droplet.parser.statement;

import com.moefrumkin.droplet.parser.Parser;
import com.moefrumkin.droplet.parser.SyntaxTree;
import com.moefrumkin.droplet.parser.exception.UnexpectedTokenException;
import com.moefrumkin.droplet.parser.exception.UnexpectedTokenTypeException;
import com.moefrumkin.droplet.token.Token;
import com.moefrumkin.droplet.token.Type;

/**
 * Interface that represents a statement.
 *
 * @implNote Because a statement type is fully determined by the first token a if else statement can be used when determining the type of statement.
 */
public interface Statement extends SyntaxTree {
	/**
	 * Parses a statement from the given parser
	 * @param parser the parser
	 * @return the statement
	 * @throws UnexpectedTokenException if a token is not matched
	 * @throws UnexpectedTokenTypeException if a token is not matched
	 */
	static Statement parse(Parser parser) throws UnexpectedTokenException, UnexpectedTokenTypeException {
		Token current = parser.currentToken();
		//switch depending on the current token
		if(current.matches(Type.SPECIAL, "{")) {
			//compound statement
			return CompoundStatement.parse(parser);
		} else if(current.matches(Type.KEYWORD, "if")) {
			//conditional statement
			return ConditionalStatement.parse(parser);
		} else if(current.matches(Type.KEYWORD, "while")) {
			//while loop
			return LoopStatement.parse(parser);
		} else if(current.matches(Type.KEYWORD, "return")) {
			//return statement
			return ReturnStatement.parse(parser);
		} else if(current.matches(Type.KEYWORD, "let")) {
			//variable declaration statement
			return DeclarationStatement.parse(parser);
		} else if(current.matches(Type.SPECIAL, ";")) {
			//no op statement
			return EmptyStatement.parse(parser);
		} else {
			//try an expression statement
			return ExpressionStatement.parse(parser);
		}
	}
}
