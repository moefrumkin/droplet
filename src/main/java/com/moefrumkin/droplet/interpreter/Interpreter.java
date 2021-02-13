package com.moefrumkin.droplet.interpreter;

import com.moefrumkin.droplet.parser.expression.*;
import com.moefrumkin.droplet.parser.statement.*;
import com.moefrumkin.droplet.parser.function.*;

/**
 * An interface that represents an interpreter for a droplet expression. This uses the <a href="https://en.wikipedia.org/wiki/Visitor_pattern">visitor pattern</a>.
 */
public interface Interpreter {

    void interpret(Function function);
    void interpret(FunctionList function);


    void interpret(CompoundStatement statement);
    void interpret(ConditionalStatement statement);
    void interpret(DeclarationStatement statement);
    void interpret(EmptyStatement statement);
    void interpret(ExpressionStatement statement);
    void interpret(LoopStatement statement);
    void interpret(ReturnStatement statement);

    int interpret(BinaryOperationExpression expression);
    int interpret(EnclosedExpression expression);
    int interpret(FunctionCallExpression expression);
    int interpret(IdentifierExpression expression);
    int interpret(LiteralExpression expression);
    int interpret(UnaryOperationExpression expression);
}
