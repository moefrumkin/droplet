package com.moefrumkin.droplet.parser.statement;

import com.moefrumkin.droplet.parser.Parser;
import com.moefrumkin.droplet.parser.exception.UnexpectedTokenException;
import com.moefrumkin.droplet.parser.exception.UnexpectedTokenTypeException;
import com.moefrumkin.droplet.parser.expression.*;
import com.moefrumkin.droplet.token.Token;
import com.moefrumkin.droplet.token.Type;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatementTest {
    static final String program = """
                {
                    let i = 5;
                    let j = -i;
                    print(i + j == 0);
                }
            """;

    static final Token i = new Token(Type.IDENTIFIER, "i");
    static final Token five = new Token(Type.LITERAL, "5");
    static final Token j = new Token(Type.IDENTIFIER, "j");
    static final Token print = new Token(Type.IDENTIFIER, "print");
    static final Token zero = new Token(Type.LITERAL, "0");

    static final Statement initI = new DeclarationStatement(i, new LiteralExpression(five));
    static final Statement initJ = new DeclarationStatement(j, new UnaryOperationExpression(UnaryOperationExpression.Type.NEGATION, new IdentifierExpression(i)));
    static final Expression iAndJ = new BinaryOperationExpression(BinaryOperationExpression.Type.ADDITION, new IdentifierExpression(i), new IdentifierExpression(j));
    static final Expression iAndJIsZero = new BinaryOperationExpression(BinaryOperationExpression.Type.EQUALS, iAndJ, new LiteralExpression(zero));
    static final Expression printCall = new FunctionCallExpression(print, List.of(iAndJIsZero));

    static final CompoundStatement compound = new CompoundStatement(List.of(
            initI,
            initJ,
            new ExpressionStatement(printCall)
    ));


    @Test
    void testStatement() throws UnexpectedTokenTypeException, UnexpectedTokenException {
        assertEquals(compound, Statement.parse(new Parser(Token.tokenize(program))));
    }
}
