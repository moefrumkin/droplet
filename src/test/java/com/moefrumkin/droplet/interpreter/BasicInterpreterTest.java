package com.moefrumkin.droplet.interpreter;

import com.moefrumkin.droplet.interpreter.basicInterpreter.BasicInterpreter;
import com.moefrumkin.droplet.parser.Parser;
import com.moefrumkin.droplet.parser.SyntaxTree;
import com.moefrumkin.droplet.parser.exception.UnexpectedTokenException;
import com.moefrumkin.droplet.parser.exception.UnexpectedTokenTypeException;
import com.moefrumkin.droplet.token.Token;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;


public class BasicInterpreterTest {
    String program = """
                def main() {
                    print(0);
                    print(double(5));
                    print(factorial(0));
                    print(factorial(5));
                }
                
                def double(n) {
                    return 2 * n;
                }
                
                def factorial(n) {
                    if(n == 0)
                        return 1;
                    return n * factorial(n - 1);
                }
            """;

    @Test
    public void testRun() throws UnexpectedTokenTypeException, UnexpectedTokenException {
        SyntaxTree tree = new Parser(Token.tokenize(program)).parse();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Interpreter interpreter = new BasicInterpreter(outputStream);
        tree.interpret(interpreter);
        assertArrayEquals(new byte[]{'0', '1', '0', '1', '1', '2', '0'}, outputStream.toByteArray());
    }
}
