package com.moefrumkin.droplet.token;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TokenTest {
    private final String program = """
                let n = 5;
            """;

    private final Token blank = new Token(Type.WHITESPACE, " ");
    private final Token alsoBlank = new Token(Type.WHITESPACE, " ");

    private final Token let = new Token(Type.KEYWORD, "let");
    private final Token n = new Token(Type.IDENTIFIER, "n");
    private final Token assign = new Token(Type.OPERATOR, "=");
    private final Token five = new Token(Type.LITERAL, "5");
    private final Token semicolon = new Token(Type.SPECIAL, ";");
    private final Token terminator = new Token(Type.TERMINATOR, "");

    private final List<Token> tokenized = Token.tokenize(program);

    @Test
    public void construction() {
        assertThrows(IllegalArgumentException.class, () -> new Token(Type.IDENTIFIER, "ha ha"));
        assertThrows(IllegalArgumentException.class, () -> new Token(Type.LITERAL, "3,600"));
        assertThrows(IllegalArgumentException.class, () -> new Token(Type.SPECIAL, "()"));
        assertThrows(IllegalArgumentException.class, () -> new Token(Type.OPERATOR, "^"));
        assertThrows(IllegalArgumentException.class, () -> new Token(Type.KEYWORD, "throw"));
    }

    @Test
    public void equality() {
        assertEquals(alsoBlank, blank);
        assertNotEquals(let, blank);
        assertEquals(blank.hashCode(), alsoBlank.hashCode());
    }

    @Test
    public void match() {
        assertTrue(blank.matches(Type.WHITESPACE));
        assertTrue(let.matches(Type.KEYWORD, "let"));
        assertTrue(n.matches(Type.IDENTIFIER, List.of("int", "n", "val")));
    }

    @Test
    public void tokenize() {
        assertEquals(List.of(
            let, n, assign, five, semicolon, terminator
        ), tokenized);
    }
}
