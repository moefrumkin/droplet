package com.moefrumkin.droplet.parser.statement;


import com.moefrumkin.droplet.interpreter.Interpreter;
import com.moefrumkin.droplet.parser.expression.Expression;
import com.moefrumkin.droplet.parser.Parser;
import com.moefrumkin.droplet.parser.exception.UnexpectedTokenException;
import com.moefrumkin.droplet.parser.exception.UnexpectedTokenTypeException;
import com.moefrumkin.droplet.token.Type;

/**
 * A conditional statement has the form if (Expression) Statement
 */
public record ConditionalStatement(Expression condition, Statement consequence) implements Statement {

    /**
     * Parses a conditional statement
     * @param parser the parser
     * @return the statement
     * @throws UnexpectedTokenException if an unexpected token is seen
     * @throws UnexpectedTokenTypeException if an unexpected token type is seen
     */
    public static ConditionalStatement parse(Parser parser) throws UnexpectedTokenException, UnexpectedTokenTypeException {
        //match if keyword
        parser.match(Type.KEYWORD, "if");

        //match open paren
        parser.match(Type.SPECIAL, "(");

        //parse expression
        Expression condition = Expression.parse(parser);

        //match close paren
        parser.match(Type.SPECIAL, ")");

        //parse statement
        Statement consequence = Statement.parse(parser);

        return new ConditionalStatement(condition, consequence);
    }

    @Override
    public void interpret(Interpreter interpreter){
        interpreter.interpret(this);
    }

    @Override
    public String toString() {
        return "( if " + condition + " " + consequence + " )";
    }

}
