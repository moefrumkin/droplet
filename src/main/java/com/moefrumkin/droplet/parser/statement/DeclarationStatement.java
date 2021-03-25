package com.moefrumkin.droplet.parser.statement;

import com.moefrumkin.droplet.interpreter.Interpreter;
import com.moefrumkin.droplet.parser.expression.Expression;
import com.moefrumkin.droplet.parser.Parser;
import com.moefrumkin.droplet.parser.exception.UnexpectedTokenException;
import com.moefrumkin.droplet.parser.exception.UnexpectedTokenTypeException;
import com.moefrumkin.droplet.parser.expression.LiteralExpression;
import com.moefrumkin.droplet.token.Token;
import com.moefrumkin.droplet.token.Type;

import java.util.Optional;


/**
 * A Declaration statement has the form let Identifier | let Identifier = Expression
 */
public record DeclarationStatement(Token identifier, Expression value) implements Statement {

    /**
     * The default value if none is specified
     */
    public static Expression DEFAULT_VALUE = new LiteralExpression(new Token(Type.LITERAL, "0"));

    /**
     * Parses a declaration statement
     * @param parser the parser
     * @return the statement
     * @throws UnexpectedTokenException if an unexpected token is seen
     * @throws UnexpectedTokenTypeException if an unexpected token type is seen
     */
    public static DeclarationStatement parse(Parser parser) throws UnexpectedTokenException, UnexpectedTokenTypeException {
        //expect keyword let
        parser.match(Type.KEYWORD, "let");

        //get identifier
        Token identifier = parser.match(Type.IDENTIFIER);

        //check if there is an equals sign
        if (parser.currentToken().matches(Type.OPERATOR, "=")) {
            //expect equals sign
            parser.match(Type.OPERATOR, "=");

            //get expression
            Expression value = Expression.parse(parser);

            //expect semicolon
            parser.match(Type.SPECIAL, ";");

            return new DeclarationStatement(identifier, value);
        }

        //expect semicolon
        parser.match(Type.SPECIAL, ";");

        return new DeclarationStatement(identifier);
    }

    /**
     * Creates a declaration statement with the default value
     * @param identifier the identifier token
     */
    public DeclarationStatement(Token identifier) {
        this(identifier, DEFAULT_VALUE);
    }

    @Override
    public void interpret(Interpreter interpreter){
        interpreter.interpret(this);
    }

    @Override
    public String toString() {
        return "( let " + identifier.getData() + " " + value + " )";
    }

}
