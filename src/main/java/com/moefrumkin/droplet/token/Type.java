package com.moefrumkin.droplet.token;

/**
 * An enum that represents the types of token in droplet
 */
public enum Type {
    /**
     * A space, tab, or newline character. These can generally be ignored
     */
    WHITESPACE("[\s\t\f\r\n]+"),
    /**
     * A control flow keyword
     */
    KEYWORD("return|while|if|let|def"),
    /**
     * A variable or function name
     */
    IDENTIFIER("[A-Za-z]+"),
    /**
     * An integer literal
     */
    LITERAL("-?[0-9]+"),
    /**
     * Special characters that are used for newlines, scopes, parameters and expressions
     */
    SPECIAL("[{}\\();,]"),
    /**
     * Operators
     */
    OPERATOR("==|!=|\\|\\||\\&\\&|!|[+\\-*<>=]");

    private final String pattern;

    Type(String pattern) {
        this.pattern = pattern;
    }

    /**
     * This function returns a regular expression that recognizes the valid strings of each token type
     * @return the regex as a {@link String}
     */
    public String getPattern() { return pattern; }
}
