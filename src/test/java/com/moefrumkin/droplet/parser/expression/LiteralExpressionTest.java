package com.moefrumkin.droplet.parser.expression;

import com.moefrumkin.droplet.token.Token;
import com.moefrumkin.droplet.token.Type;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class LiteralExpressionTest {
    final Token five = new Token(Type.LITERAL, "5");
    final Token print = new Token(Type.IDENTIFIER, "print");
    final Token alsoPrint = new Token(Type.IDENTIFIER, "print");

    final LiteralExpression fiveExp = new LiteralExpression(five);
    final LiteralExpression printExp = new LiteralExpression(print);
    final LiteralExpression printCopy = new LiteralExpression(print);
    final LiteralExpression alsoPrintExp = new LiteralExpression(alsoPrint);

    @Test
    void testLiteralExpressions() {
        assertEquals(fiveExp, fiveExp);
        assertEquals(printExp, printCopy);
        assertEquals(printExp, alsoPrintExp);

        assertNotEquals(fiveExp, printExp);
        assertNotEquals(alsoPrintExp, fiveExp);
    }
}
