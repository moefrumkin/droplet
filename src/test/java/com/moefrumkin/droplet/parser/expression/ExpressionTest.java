package com.moefrumkin.droplet.parser.expression;

import com.moefrumkin.droplet.parser.Parser;
import com.moefrumkin.droplet.parser.exception.UnexpectedTokenException;
import com.moefrumkin.droplet.parser.exception.UnexpectedTokenTypeException;
import com.moefrumkin.droplet.token.Token;
import com.moefrumkin.droplet.token.Type;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExpressionTest {
    final String expression1 = "-i;";
    final String expression2 = "print(i + j == 0);";
    final String expression3 = "i + j == 0;";

    static final Token i = new Token(Type.IDENTIFIER, "i");
    static final Token j = new Token(Type.IDENTIFIER, "j");
    static final Token print = new Token(Type.IDENTIFIER, "print");
    static final Token zero = new Token(Type.LITERAL, "0");

    static final Expression negativeI = new UnaryOperationExpression(UnaryOperationExpression.Type.NEGATION, new IdentifierExpression(i));
    static final Expression iAndJ = new BinaryOperationExpression(BinaryOperationExpression.Type.ADDITION, new IdentifierExpression(i), new IdentifierExpression(j));
    static final BinaryOperationExpression iAndJIsZero = new BinaryOperationExpression(BinaryOperationExpression.Type.EQUALS, iAndJ, new LiteralExpression(zero));
    static final FunctionCallExpression printCall = new FunctionCallExpression(print, List.of(iAndJIsZero));

    @Test
    public void testExpressionParse() throws UnexpectedTokenTypeException, UnexpectedTokenException {
        Expression negate = Expression.parse(new Parser(Token.tokenize(expression1)));
        FunctionCallExpression print = (FunctionCallExpression) Expression.parse(new Parser(Token.tokenize(expression2)));
        BinaryOperationExpression isZero = (BinaryOperationExpression) Expression.parse(new Parser(Token.tokenize(expression3)));


        assertEquals(negativeI, negate);
        assertEquals(printCall.identifier(), print.identifier());
        assertEquals(iAndJIsZero.type(), isZero.type());
        assertEquals(iAndJIsZero.left(), isZero.left());
        assertEquals(new LiteralExpression(new Token(Type.LITERAL, "0")), isZero.right());
        assertEquals(new LiteralExpression(new Token(Type.LITERAL, "0")), iAndJIsZero.right());
        assertEquals(iAndJIsZero.right(), isZero.right());
        assertEquals(iAndJIsZero, isZero);
        assertEquals(printCall.arguments().get(0), print.arguments().get(0));
        assertEquals(printCall.arguments(), print.arguments());
        assertEquals(printCall, print);
    }
}
