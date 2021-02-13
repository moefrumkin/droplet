package com.moefrumkin.droplet.parser;

import com.moefrumkin.droplet.parser.function.FunctionList;
import com.moefrumkin.droplet.parser.exception.UnexpectedTokenException;
import com.moefrumkin.droplet.parser.exception.UnexpectedTokenTypeException;
import com.moefrumkin.droplet.token.Token;
import com.moefrumkin.droplet.token.Type;
import com.moefrumkin.droplet.parser.function.Function;

import java.util.ArrayList;
import java.util.List;

/**
 * A parser that parses the droplet language. This is done by iterating over a list of tokens. A program is a list of functions. Parsing a function is done by the {@link Function} class.
 */
public class Parser {

    private final List<Token> tokens;
    private int tokenIndex;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        tokenIndex = 0;
    }

    /**
     * This function returns the current token that the parser is looking at
     * @return the {@link Token}
     */
    public Token currentToken() {
        return tokens.get(tokenIndex);
    }

    /**
     * Increments the index of the current token that is being looked at
     */
    public void incrementIndex() {
        tokenIndex += 1;
    }

    /**
     * This function returns the current token if it has the given type and then increments the index of the token. If the type is not matches then it throw an exception
     * @param type The {@link Type} that the current token must be
     * @return The current {@link Token} if it matches the given type
     * @throws UnexpectedTokenTypeException if the current token does not have the given type
     */
    public Token match(Type type) throws UnexpectedTokenTypeException {
        Token current = currentToken();
        if(current.matches(type)) {
            incrementIndex();
            return current;
        } else {
            throw new UnexpectedTokenTypeException(current, type);
        }
    }

    /**
     * This function returns the current token if it has the given type and data and then increments the index of the token. If the type and data are not matches it throws an exception
     * @param type the required {@link Type} of the token
     * @param data the required data of the token
     * @return the token if the type and data match
     * @throws UnexpectedTokenException if the token does not match the type and data
     */
    public Token match(Type type, String data) throws UnexpectedTokenException {
        Token current = currentToken();
        if(current.matches(type, data)) {
            incrementIndex();
            return current;
        } else {
            throw new UnexpectedTokenException(current, type, data);
        }
    }

    /**
     *
     * @return True if and only if there are tokens left to parse
     */
    public boolean tokensLeft() {
        return tokenIndex < tokens.size();
    }

    /**
     * Begins the parsing process. A program is a list of functions. While there are still tokens the parser tries to parse a function.
     * @return The {@link SyntaxTree} that represents the program
     * @throws UnexpectedTokenTypeException if a token is not matched
     * @throws  UnexpectedTokenException if a token is not matched
     */
    public SyntaxTree parse() throws UnexpectedTokenTypeException, UnexpectedTokenException {
        //init empty list of functions
        List<Function> functions = new ArrayList<>();

        while(tokensLeft()) {
            functions.add(Function.parse(this));
        }

        return new FunctionList(functions);
    }

}
