package com.moefrumkin.droplet.parser.function;

import java.util.ArrayList;
import java.util.List;

import com.moefrumkin.droplet.interpreter.Interpreter;
import com.moefrumkin.droplet.parser.Parser;
import com.moefrumkin.droplet.parser.statement.Statement;
import com.moefrumkin.droplet.parser.SyntaxTree;
import com.moefrumkin.droplet.parser.exception.UnexpectedTokenException;
import com.moefrumkin.droplet.parser.exception.UnexpectedTokenTypeException;
import com.moefrumkin.droplet.token.Token;
import com.moefrumkin.droplet.token.Type;

/**
 * A class that represents a function definition
 */
public record Function(Token name, List<Token> parameters, Statement body) implements SyntaxTree {

    /**
     * Static method that parses a function definition
     *
     * @param parser The {@link Parser} containing the list of tokens
     * @return The function definition object
     * @throws UnexpectedTokenException     If a token is not matched
     * @throws UnexpectedTokenTypeException If a token lacks the required {@link Type}
     */
    public static Function parse(Parser parser) throws UnexpectedTokenException, UnexpectedTokenTypeException {
        //expect let keyword
        parser.match(Type.KEYWORD, "def");
        Token name = parser.match(Type.IDENTIFIER);
        //expect opening paren
        parser.match(Type.SPECIAL, "(");

        //get parameters
        List<Token> parameters = new ArrayList<>();
        //do until close paren found
        while (!(parser.currentToken().getType().equals(Type.SPECIAL) && parser.currentToken().getData().equals(")"))) {
            //add a new parameter
            parameters.add(parser.match(Type.IDENTIFIER));
            // require a comma unless the current parameter is the final one in the list
            if (!parser.currentToken().matches(Type.SPECIAL, ")")) {
                parser.match(Type.SPECIAL, ",");
            }
        }
        //expect a close paren
        parser.match(Type.SPECIAL, ")");
        //parse a compound statement
        Statement body = Statement.parse(parser);

        return new Function(name, parameters, body);
    }

    public void interpret(Interpreter interpreter) {
        interpreter.interpret(this);
    }

    @Override
    public String toString() {
        return "( def " + name.getData() + " " + parameters + " " + body + " )";
    }

}
