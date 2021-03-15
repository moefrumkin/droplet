package com.moefrumkin.droplet.parser.expression;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.moefrumkin.droplet.interpreter.Interpreter;
import com.moefrumkin.droplet.parser.Parser;
import com.moefrumkin.droplet.parser.SyntaxTree;
import com.moefrumkin.droplet.parser.exception.UnexpectedTokenException;
import com.moefrumkin.droplet.parser.exception.UnexpectedTokenTypeException;
import com.moefrumkin.droplet.token.Token;
import com.moefrumkin.droplet.token.Type;

/**
 * An interface that represents expressions.
 */
public interface Expression extends SyntaxTree {

    /**
     * Evaluate the expression with the given interpreter
     *
     * @param interpreter the interpreter
     * @return the integer value of the interpreter
     */
    int evaluate(Interpreter interpreter);

    @Override
    default void interpret(Interpreter interpreter) {
        evaluate(interpreter);
    }

    Map<String, Integer> OPERATOR_PRECEDENCE = Map.ofEntries(
            Map.entry("++", 2),
            Map.entry("--", 2),
            Map.entry("*", 3),
            Map.entry("+", 4),
            Map.entry("-", 5),
            Map.entry("<", 6),
            Map.entry(">", 6),
            Map.entry("==", 7),
            Map.entry("!=", 7),
            Map.entry("=", 14)

    );

    Set<String> UNARY_OPERATORS = Set.of("!", "-", "++", "--");
    Set<String> BINARY_OPERATORS = Set.of("==", "!=", "||", "&&", "+", "-", "*", "=", "<", ">");

    /**
     * This function parses an expression.
     *
     * @param parser The parser to parse
     * @return The expression produced
     * @throws UnexpectedTokenTypeException If a token has an unexpected type
     * @throws UnexpectedTokenException     If a token is unexpected
     * @implNote The grammar of droplet expressions is ambiguous, so the <a href="https://en.wikipedia.org/wiki/Shunting-yard_algorithm"> Shunting-yard algorithm </a> is used to determine the order of operations.
     */
    static Expression parse(Parser parser) throws UnexpectedTokenTypeException, UnexpectedTokenException {
        //initialize stacks for shunting yard algorithm
        Deque<Expression> dataStack = new ArrayDeque<>();
        Deque<Token> operatorStack = new ArrayDeque<>();

        boolean parsing = true;

        while (parsing) {
            //switch depending on current token
            Token current = parser.currentToken();
            if (current.matches(Type.LITERAL)) {
                dataStack.push(new LiteralExpression(parser.match(Type.LITERAL)));
            } else if (current.matches(Type.OPERATOR)) {
                //switch depending on precedence
                //check if operator stack is empty
                if (operatorStack.isEmpty()) {
                    operatorStack.push(current);
                    //move parser ahead
                    parser.match(Type.OPERATOR);
                } else if (OPERATOR_PRECEDENCE.get(current.getData()) <= OPERATOR_PRECEDENCE.get(operatorStack.peek().getData())) {
                    //the operator has a higher precedence(lower number), so push it onto the stack
                    operatorStack.push(current);
                    //move parser ahead
                    parser.match(Type.OPERATOR);
                } else {
                    //the operator has a lower precedence, so reduce the stack with what is on the stack
                    reduce(dataStack, operatorStack.pop());
                    //push current onto the stack
                    operatorStack.push(current);
                    //more parse ahead
                    parser.match(Type.OPERATOR);
                }
            } else if (current.matches(Type.IDENTIFIER)) {
                // identifier use
                Token identifier = parser.match(Type.IDENTIFIER);

                if (parser.currentToken().matches(Type.SPECIAL, "(")) {
                    // function call
                    //match open paren
                    parser.match(Type.SPECIAL, "(");
                    List<Expression> arguments = new ArrayList<>();
                    while (!parser.currentToken().matches(Type.SPECIAL, ")")) {
                        arguments.add(Expression.parse(parser));
                        // require a comma unless the current parameter is the final one in the list
                        if (!parser.currentToken().matches(Type.SPECIAL, ")")) {
                            parser.match(Type.SPECIAL, ",");
                        }
                    }
                    // expect closing paren
                    parser.match(Type.SPECIAL, ")");
                    // add the function to the data stack
                    dataStack.push(new FunctionCallExpression(identifier, arguments));
                } else {
                    // variable reference
                    dataStack.push(new IdentifierExpression(identifier));
                }
            } else {
                // end of expression reached
                parsing = false;
            }

        }

        //reduce the data stack until the op stack is empty
        while (!operatorStack.isEmpty()) {
            reduce(dataStack, operatorStack.pop());
        }

        //return what is left on the data stack
        return dataStack.pop();
    }

    /**
     * Reduces the data stack using the given operation
     *
     * @param dataStack The data stack
     * @param operation The operation
     */
    private static void reduce(Deque<Expression> dataStack, Token operation) {
        //check stack length
        if (dataStack.isEmpty()) {
            throw new IllegalArgumentException("Cannot reduce an empty stack");
        } else {
            //special case for token "-" which is both binary and unary
            if (operation.matches(Type.OPERATOR, "-")) {
                //unary case
                if (dataStack.size() == 1) {
                    dataStack.push(new UnaryOperationExpression(UnaryOperationExpression.typeFromString(operation.getData()), dataStack.pop()));
                } else {
                    //right and left must be gotten first, because leftmost element gets pushed further down the stack
                    Expression right = dataStack.pop();
                    Expression left = dataStack.pop();
                    dataStack.push(new BinaryOperationExpression(BinaryOperationExpression.typeFromString(operation.getData()), left, right));
                }
            } else if (UNARY_OPERATORS.contains(operation.getData())) {
                //current is a unary operator
                dataStack.push(new UnaryOperationExpression(UnaryOperationExpression.typeFromString(operation.getData()), dataStack.pop()));
            } else if (BINARY_OPERATORS.contains(operation.getData())) {
                //right and left must be gotten first, because leftmost element gets pushed further down the stack
                Expression right = dataStack.pop();
                Expression left = dataStack.pop();
                dataStack.push(new BinaryOperationExpression(BinaryOperationExpression.typeFromString(operation.getData()), left, right));
            } else {
                throw new IllegalArgumentException("Operation type " + operation + " is not recognized");
            }
        }
    }
}
