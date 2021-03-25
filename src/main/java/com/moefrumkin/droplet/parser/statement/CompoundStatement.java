package com.moefrumkin.droplet.parser.statement;

import com.moefrumkin.droplet.interpreter.Interpreter;
import com.moefrumkin.droplet.parser.Parser;
import com.moefrumkin.droplet.parser.exception.UnexpectedTokenException;
import com.moefrumkin.droplet.parser.exception.UnexpectedTokenTypeException;
import com.moefrumkin.droplet.token.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * A compound statement is a list of statements grouped between curly braces
 */
public record CompoundStatement(List<Statement> statements) implements Statement {

    /**
     * Parses a compound statement from a parser
     * @param parser the parser
     * @return The compound statement
     * @throws UnexpectedTokenException if an unexpected token is seen
     * @throws UnexpectedTokenTypeException if an unexpected token type is seen
     */
    public static CompoundStatement parse(Parser parser) throws UnexpectedTokenException, UnexpectedTokenTypeException {
        //expect and opening bracket
        parser.match(Type.SPECIAL, "{");

        //init statements
        List<Statement> statements = new ArrayList<>();

        //while not at end
        while (!parser.currentToken().matches(Type.SPECIAL, "}")) {
            //parse current statement
            statements.add(Statement.parse(parser));
        }

        //match closing bracket
        parser.match(Type.SPECIAL, "}");

        return new CompoundStatement(statements);
    }

    @Override
    public void interpret(Interpreter interpreter){
        interpreter.interpret(this);
    }

    @Override
    public String toString() {
        return statements.toString();
    }

}
