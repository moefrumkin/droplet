package com.moefrumkin.droplet.parser;

import com.moefrumkin.droplet.parser.exception.UnexpectedTokenException;
import com.moefrumkin.droplet.parser.exception.UnexpectedTokenTypeException;
import com.moefrumkin.droplet.parser.expression.*;
import com.moefrumkin.droplet.parser.function.Function;
import com.moefrumkin.droplet.parser.function.FunctionList;
import com.moefrumkin.droplet.parser.statement.CompoundStatement;
import com.moefrumkin.droplet.parser.statement.DeclarationStatement;
import com.moefrumkin.droplet.parser.statement.ExpressionStatement;
import com.moefrumkin.droplet.parser.statement.Statement;
import com.moefrumkin.droplet.token.Token;
import com.moefrumkin.droplet.token.Type;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class ParserTest {
    static final String program = """
                def main() {
                    let i = 5;
                    let j = -i;
                    print(i + j == 0);
                }
            """;

    static final Token def = new Token(Type.KEYWORD, "def");
    static final Token main = new Token(Type.IDENTIFIER, "main");
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

    static final Statement mainBody = new CompoundStatement(List.of(
            initI,
            initJ,
            new ExpressionStatement(printCall)
    ));

    static final SyntaxTree parsedProgram = new FunctionList(List.of(
            new Function(main, List.of(), mainBody)
    ));

    static final List<Token> tokenization = Token.tokenize(program);
    static final Parser parser = new Parser(tokenization);
    static final Parser testParser = new Parser(tokenization);

    @Test
    public void testParserMatch() throws UnexpectedTokenTypeException, UnexpectedTokenException {
        Assertions.assertEquals(def, testParser.match(Type.KEYWORD));
        Assertions.assertEquals(main, testParser.match(Type.IDENTIFIER, "main"));
        assertThrows(UnexpectedTokenTypeException.class, () -> testParser.match(Type.KEYWORD));
        assertThrows(UnexpectedTokenException.class, () -> testParser.match(Type.SPECIAL, "{"));
    }

    @Test
    public void testParsing() throws UnexpectedTokenTypeException, UnexpectedTokenException {
        SyntaxTree syntaxTree = parser.parse();
        assertEquals(parsedProgram, syntaxTree);
        assertFalse(parser.tokensLeft());
    }
}
