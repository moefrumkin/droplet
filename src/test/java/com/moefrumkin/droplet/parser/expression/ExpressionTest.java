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

    @Test
    public void testExpressionParse() throws UnexpectedTokenTypeException, UnexpectedTokenException {
        final String expression1 = "-i;";
        final String expression2 = "print(i + j == 0);";
        final String expression3 = "i + j == 0;";

        final Token i = new Token(Type.IDENTIFIER, "i");
        final Token j = new Token(Type.IDENTIFIER, "j");
        final Token print = new Token(Type.IDENTIFIER, "print");
        final Token zero = new Token(Type.LITERAL, "0");

        final Expression negativeI = new UnaryOperationExpression(UnaryOperationExpression.Type.NEGATION, new IdentifierExpression(i));
        final Expression iAndJ = new BinaryOperationExpression(BinaryOperationExpression.Type.ADDITION, new IdentifierExpression(i), new IdentifierExpression(j));
        final BinaryOperationExpression iAndJIsZero = new BinaryOperationExpression(BinaryOperationExpression.Type.EQUALS, iAndJ, new LiteralExpression(zero));
        final FunctionCallExpression printCall = new FunctionCallExpression(print, List.of(iAndJIsZero));
        final Expression negate = Expression.parse(new Parser(Token.tokenize(expression1)));
        final FunctionCallExpression printExpression = (FunctionCallExpression) Expression.parse(new Parser(Token.tokenize(expression2)));
        final BinaryOperationExpression isZero = (BinaryOperationExpression) Expression.parse(new Parser(Token.tokenize(expression3)));


        assertEquals(negativeI, negate);
        assertEquals(printCall.identifier(), printExpression.identifier());
        assertEquals(iAndJIsZero.type(), isZero.type());
        assertEquals(iAndJIsZero.left(), isZero.left());
        assertEquals(new LiteralExpression(new Token(Type.LITERAL, "0")), isZero.right());
        assertEquals(new LiteralExpression(new Token(Type.LITERAL, "0")), iAndJIsZero.right());
        assertEquals(iAndJIsZero.right(), isZero.right());
        assertEquals(iAndJIsZero, isZero);
        assertEquals(printCall.arguments().get(0), printExpression.arguments().get(0));
        assertEquals(printCall.arguments(), printExpression.arguments());
        assertEquals(print, print);
    }

    @Test
    public void testNegation() throws UnexpectedTokenTypeException, UnexpectedTokenException {
        final String negation = "1 - - 1;";
        final Expression one = new LiteralExpression(new Token(Type.LITERAL, "1"));
        final Expression negativeOne = new UnaryOperationExpression(UnaryOperationExpression.Type.NEGATION, one);

        final Expression oneMinusNegativeOne = new BinaryOperationExpression(BinaryOperationExpression.Type.SUBTRACTION, one, negativeOne);

        assertEquals(negativeOne, Expression.parse(new Parser(Token.tokenize("- 1"))));
        assertEquals(oneMinusNegativeOne, Expression.parse(new Parser(Token.tokenize(negation))));
    }
}
