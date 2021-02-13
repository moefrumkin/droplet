package com.moefrumkin.droplet.parser.statement;


import com.moefrumkin.droplet.interpreter.Interpreter;
import com.moefrumkin.droplet.parser.Parser;
import com.moefrumkin.droplet.parser.exception.UnexpectedTokenException;
import com.moefrumkin.droplet.token.Type;

/**
 * An empty or no op statement is just a semicolon ;)
 */
public class EmptyStatement implements Statement {
	
	//single instance to enforce singleton nature
	public static final EmptyStatement emptyStatement = new EmptyStatement();

	private EmptyStatement() {}
	
	public static EmptyStatement parse(Parser parser) throws UnexpectedTokenException {
		//expect semicolon
		parser.match(Type.SPECIAL, ";");
		
		return emptyStatement;
	}

	@Override
	public void interpret(Interpreter interpreter){
		interpreter.interpret(this);
	}
	
	@Override
	public String toString() {
		return "e";
	}

}
