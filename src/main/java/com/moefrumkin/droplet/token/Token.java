package com.moefrumkin.droplet.token;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A record class that represents a lexical token in droplet
 */
public class Token {

    /**
     * Tokenizes a string. This will break up a string into a series of tokens that can be parsed.
     * <i>Whitespace tokens will not be returned</i>
     *
     * @param source The string to tokenize
     * @return A {@link List} of Tokens that have been generated from the input string
     */
    public static List<Token> tokenize(String source) {
        ArrayList<Token> tokens = new ArrayList<>();

        StringBuilder tokenPatternsBuilder = new StringBuilder();
        for (Type type : Type.values()) {
            tokenPatternsBuilder.append(String.format("|(?<%s>%s)", type.name(), type.getPattern()));
        }
        Pattern tokenPatterns = Pattern.compile(tokenPatternsBuilder.substring(1));

        Matcher matcher = tokenPatterns.matcher(source);
        while (matcher.find()) {
            if (matcher.group(Type.KEYWORD.name()) != null) {
                tokens.add(new Token(Type.KEYWORD, matcher.group(Type.KEYWORD.name())));
            } else if (matcher.group(Type.IDENTIFIER.name()) != null) {
                tokens.add(new Token(Type.IDENTIFIER, matcher.group(Type.IDENTIFIER.name())));
            } else if (matcher.group(Type.LITERAL.name()) != null) {
                tokens.add(new Token(Type.LITERAL, matcher.group(Type.LITERAL.name())));
            } else if (matcher.group(Type.SPECIAL.name()) != null) {
                tokens.add(new Token(Type.SPECIAL, matcher.group(Type.SPECIAL.name())));
            } else if (matcher.group(Type.OPERATOR.name()) != null) {
                tokens.add(new Token(Type.OPERATOR, matcher.group(Type.OPERATOR.name())));
            }
        }

        return tokens;
    }

    private final Type type;
    private final String data;

    /**
     *
     * @param type The {@link Type} of the token
     * @param data The string of the token, which must follow the regex provided by the given {@link Type}
     */
    public Token(Type type, String data) {
        //enforce data follows regex
        Pattern pattern = Pattern.compile(type.getPattern());
        Matcher matcher = pattern.matcher(data);

        if(!matcher.matches())
            throw new IllegalArgumentException("The data does not match the pattern, for type " + type + ", with pattern " + type.getPattern() + " and data " + data);

        this.type = type;
        this.data = data;
    }

    /**
     * @return The {@link Type} of the token
     */
    public Type getType() {
        return type;
    }

    /**
     * This function returns the unmodified text of the token from which the type was determined
     *
     * @return The text of the token
     */
    public String getData() {
        return data;
    }

    /**
     * Compares the {@link Type} passed in to the type of the token
     *
     * @param type The {@link Type} to compare to
     * @return {@code true} if and only if this token has the {@link Type} passed in
     */
    public boolean matches(Type type) {
        return this.type.equals(type);
    }

    /**
     * Compares the {@link Type} passed in to the type of the token and the data to the data
     *
     * @param type The {@link Type} to compare to
     * @param data The text to compare to
     * @return {@code true} if and only if this token has the {@link Type} and data passed in
     */
    public boolean matches(Type type, String data) {
        return matches(type) && this.data.equals(data);
    }

    /**
     * Compares the {@link Type} passed in to the type of the token and the data to a list of allowed values
     *
     * @param type        The {@link Type} to compare to
     * @param allowedData The {@link Collection} of allowed data
     * @return {@code true} if and only if the token has the {@link Type} passed in and one of the allowed data strings
     */
    public boolean matches(Type type, Collection<String> allowedData) {
        for (String allowedDatum : allowedData) {
            if (matches(type, allowedDatum)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return type.hashCode() * data.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Token other)
            return matches(other.getType(), other.getData());
        return false;
    }

    @Override
    public String toString() {
        return "(" + type.name() + " '" + data + "')";
    }
}
