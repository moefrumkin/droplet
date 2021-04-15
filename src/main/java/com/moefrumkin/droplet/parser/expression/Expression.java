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

    /**
     * The map determining the precedence of operations
     */
    Map<String, Integer> OPERATOR_PRECEDENCE = Map.ofEntries(
            Map.entry("~", 2),
            Map.entry("++", 2),
            Map.entry("--", 2),
            Map.entry("*", 3),
            Map.entry("+", 4),
            Map.entry("-", 4),
            Map.entry("<", 6),
            Map.entry(">", 6),
            Map.entry("==", 7),
            Map.entry("!=", 7),
            Map.entry("=", 14)

    );

    /**
     * The valid unary operations
     */
    Set<String> UNARY_OPERATORS = Set.of("!", "~", "++", "--");

    /**
     * The valid binary operations
     */
    Set<String> BINARY_OPERATORS = Set.of("==", "!=", "||", "&&", "+", "-", "*", "=", "<", ">");

    /**
     * This function parses an expression.
     * The grammar of droplet expressions is ambiguous, so the <a href="https://en.wikipedia.org/wiki/Shunting-yard_algorithm"> Shunting-yard algorithm </a> is used to determine the order of operations.
     *
     * @param parser The parser to parse
     * @return The expression produced
     * @throws UnexpectedTokenTypeException If a token has an unexpected type
     * @throws UnexpectedTokenException     If a token is unexpected*/
    static Expression parse(Parser parser) throws UnexpectedTokenTypeException, UnexpectedTokenException {
        //initialize stacks for shunting yard algorithm
        Deque<Expression> dataStack = new ArrayDeque<>();
        Deque<Token> operatorStack = new ArrayDeque<>();

        boolean parsing = true;

        //if the first token is a negation
        if (parser.currentToken().getData().equals("-")) {
            //advance the parse
            Token current = parser.match(Type.OPERATOR, "-");
            dataStack.push(Expression.parse(parser));
            reduceUnary(dataStack, current);
        }

        while (parsing) {
            //switch depending on current token
            Token current = parser.currentToken();
            switch (current.getType()) {
                case LITERAL:
                    dataStack.push(new LiteralExpression(parser.match(Type.LITERAL)));
                    break;
                case OPERATOR:
                    processOperator(parser, dataStack, operatorStack, current);
                    //check for negation
                    if(parser.currentToken().matches(Type.OPERATOR, "-"))
                        reduceUnary(dataStack, parser.currentToken());
                    break;
                case IDENTIFIER:
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
                    break;
                case SPECIAL:
                    if (current.getData().equals("(")) {
                        parser.incrementIndex();
                        dataStack.push(new EnclosedExpression(Expression.parse(parser)));
                        parser.match(Type.SPECIAL, ")");
                        break;
                    }
                default:
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
     * Processes and operator using the shunting yard algorithm to determine the order
     * @param parser the parser
     * @param dataStack the data stack
     * @param operatorStack the operator stack
     * @param operator the operation
     * @throws UnexpectedTokenTypeException If an unexpected token type is seen
     */
    private static void processOperator(Parser parser, Deque<Expression> dataStack, Deque<Token> operatorStack, Token operator) throws UnexpectedTokenTypeException {
        //switch depending on precedence
        //check if operator stack is empty
        if (operatorStack.isEmpty()) {
            operatorStack.push(operator);
            //move parser ahead
            parser.match(Type.OPERATOR);
        } else if (OPERATOR_PRECEDENCE.get(operator.getData()) <= OPERATOR_PRECEDENCE.get(operatorStack.peek().getData())) {
            //the operator has a higher precedence(lower number), so push it onto the stack
            operatorStack.push(operator);
            //move parser ahead
            parser.match(Type.OPERATOR);
        } else {
            //the operator has a lower precedence, so reduce the stack with what is on the stack
            reduce(dataStack, operatorStack.pop());
            //push current onto the stack
            operatorStack.push(operator);
            //more parse ahead
            parser.match(Type.OPERATOR);
        }
        //check for negation
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
            if (UNARY_OPERATORS.contains(operation.getData()))
                reduceUnary(dataStack, operation);
            else if (BINARY_OPERATORS.contains(operation.getData()))
                reduceBinary(dataStack, operation);
            else
                throw new IllegalArgumentException("Operation type " + operation + " is not recognized");
        }
    }

    /**
     * Reduces the data stack given a binary operation
     *
     * @param dataStack the data stack
     * @param operation the operation
     */
    private static void reduceBinary(Deque<Expression> dataStack, Token operation) {
        //right and left must be gotten first, because leftmost element gets pushed further down the stack
        Expression right = dataStack.pop();
        Expression left = dataStack.pop();
        dataStack.push(new BinaryOperationExpression(BinaryOperationExpression.typeFromString(operation.getData()), left, right));
    }

    /**
     * Reduces the data stack given a unary operation
     *
     * @param dataStack the data stack
     * @param operation the operation
     */
    private static void reduceUnary(Deque<Expression> dataStack, Token operation) {
        dataStack.push(new UnaryOperationExpression(UnaryOperationExpression.typeFromString(operation.getData()), dataStack.pop()));
    }

}
