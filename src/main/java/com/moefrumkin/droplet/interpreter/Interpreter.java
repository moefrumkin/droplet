package com.moefrumkin.droplet.interpreter;

import com.moefrumkin.droplet.parser.expression.*;
import com.moefrumkin.droplet.parser.statement.*;
import com.moefrumkin.droplet.parser.function.*;

/**
 * An interface that represents an interpreter for a droplet expression. This uses the <a href="https://en.wikipedia.org/wiki/Visitor_pattern">visitor pattern</a>.
 */
public interface Interpreter {

    /**
     * Interpret a function
     * @param function the function
     */
    void interpret(Function function);

    /**
     * Interpret a list of functions
     * @param function the list of functions
     */
    void interpret(FunctionList function);

    /**
     * Interpret a compound statement
     * @param statement the compound statement
     */
    void interpret(CompoundStatement statement);

    /**
     * Interpret a conditional statement
     * @param statement the conditional statement
     */
    void interpret(ConditionalStatement statement);

    /**
     * Interpret a declaration statement
     * @param statement the declaration statement
     */
    void interpret(DeclarationStatement statement);

    /**
     * Interpret an empty statement
     * @param statement the empty statement
     */
    void interpret(EmptyStatement statement);

    /**
     * Interpret an expression statement
     * @param statement the expression statement
     */
    void interpret(ExpressionStatement statement);

    /**
     * Interpret a loop statement
     * @param statement the loop statement
     */
    void interpret(LoopStatement statement);

    /**
     * Interpret a return statement
     * @param statement the return statement
     */
    void interpret(ReturnStatement statement);

    /**
     * Interpret a binary operation expression
     * @param expression the binary operation expression
     * @return the value of the expression
     */
    int interpret(BinaryOperationExpression expression);

    /**
     * Interpret an enclosed expression
     * @param expression the enclosed expression
     * @return the value of the expression
     */
    int interpret(EnclosedExpression expression);

    /**
     * Interpret a function call expression
     * @param expression the function call expression
     * @return the value of the expression
     */
    int interpret(FunctionCallExpression expression);

    /**
     * Interpret an identifier expression
     * @param expression the identifier expression
     * @return the value of the expression
     */
    int interpret(IdentifierExpression expression);

    /**
     * Interpret a literal expression
     * @param expression the literal expression
     * @return the value of the expression
     */
    int interpret(LiteralExpression expression);

    /**
     * Interpret a unary operation expression
     * @param expression the unary operation expression
     * @return the value of the expression
     */
    int interpret(UnaryOperationExpression expression);
}
