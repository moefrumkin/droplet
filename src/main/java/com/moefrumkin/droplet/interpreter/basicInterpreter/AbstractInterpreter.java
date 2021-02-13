package com.moefrumkin.droplet.interpreter.basicInterpreter;
import com.moefrumkin.droplet.interpreter.Interpreter;
import com.moefrumkin.droplet.interpreter.basicInterpreter.exceptions.InterpreterRuntimeException;
import com.moefrumkin.droplet.parser.expression.*;
import com.moefrumkin.droplet.parser.function.Function;
import com.moefrumkin.droplet.parser.function.FunctionList;
import com.moefrumkin.droplet.parser.statement.*;
import com.moefrumkin.droplet.token.Token;
import com.moefrumkin.droplet.token.Type;

import java.util.*;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public abstract class AbstractInterpreter implements Interpreter {

    /**
     * The predicate that determines whether a value is truthy
     * @param value the value to determine the truthiness of
     * @return the truthiness of the value
     */
    public abstract boolean truthy(int value);

    /**
     * Adds a variable with the given name and value
     * @param name the name of the variable
     * @param value the initial value of the variable
     */
    public abstract void addVariable(String name, int value);

    /**
     * Gets a variable defined in an enclosing scope
     * @param name the name of the variable
     * @return the value of the variable
     */
    public abstract int getVariable(String name);

    /**
     * Defines a function in the current scope
     * @param function the function to define
     */
    public abstract void addFunction(Function function);

    /**
     * Get a function with the given name
     * @param name the name of the function
     * @return the function
     */
    public abstract Function getFunction(String name);

    /**
     * The function that determines how a binary operator should be evaluated
     * @param type the type of the operation
     * @return the function that preforms the operation
     */
    public abstract IntBinaryOperator getBinaryOperator(BinaryOperationExpression.Type type);

    /**
     * The function that determines how a unary operator should be evaluated
     * @param type the type of the operation
     * @return the function that preforms the operation
     */
    public abstract IntUnaryOperator getUnaryOperator(UnaryOperationExpression.Type type);

    /**
     * Update the state to set a variable with the given name to a given values
     * @param name the variable to assign
     * @param value the value to assign to the variable
     */
    public abstract void assign(String name, int value);

    /**
     * Enter a new lexical scope
     */
    public abstract void enterScope();

    /**
     * Enter a new function scope
     */
    public abstract void enterFunctionScope();

    /**
     * Exit the current scope
     */
    public abstract void exitScope();

    /**
     * Return a value to the nearest function scope
     * @param value the value to return
     */
    public abstract void returnValue(int value);

    /**
     * get the return value of the nearest function scope
     * @return the return value
     */
    public abstract int getReturnValue();

    /**
     * get whether the current function should return
     * @return whether the function should return
     */
    public abstract boolean getToReturn();

    /**
     * Get a default library function from the interpreter. This
     * @param name the function's name
     * @return the function, or empty if it does not exist
     */
    public abstract Optional<ToIntFunction<List<Integer>>> getLibraryFunction(String name);


    @Override
    public void interpret(Function function) {
        addFunction(function);

        //call the function
        interpret(new FunctionCallExpression(function.name(), new ArrayList<>()));
    }

    @Override
    public void interpret(FunctionList function) {
        //add each of the functions to the frame;
        function.functions().forEach(this::addFunction);

        //get main function
        new FunctionCallExpression(new Token(Type.IDENTIFIER, "main"), new ArrayList<>()).evaluate(this);

    }

    @Override
    public void interpret(CompoundStatement statement) {
        for(Statement s: statement.statements()){
            s.interpret(this);
            //check if the previous statement returned something
            //this is necessary to prevent the return value from being overwritten
            if(getToReturn())
                return;
        }
    }

    @Override
    public void interpret(ConditionalStatement statement) {
        //evaluate the condition
        int condition = statement.condition().evaluate(this);
        //enter new lexical scope
        enterScope();
        if (truthy(condition))
            statement.consequence().interpret(this);
        //exit scope
        exitScope();
    }

    @Override
    public void interpret(DeclarationStatement statement) {
        String name = statement.identifier().getData();
        int value = statement.value().evaluate(this);
        addVariable(name, value);
    }

    @Override
    public void interpret(EmptyStatement statement) {}

    @Override
    public void interpret(ExpressionStatement statement) {
        statement.expression().evaluate(this);
    }

    @Override
    public void interpret(LoopStatement statement) {
        int condition = statement.condition().evaluate(this);

        while (truthy(condition)) {
            statement.body().interpret(this);
            condition = statement.condition().evaluate(this);
        }
    }

    @Override
    public void interpret(ReturnStatement statement) {
        returnValue(statement.expression().evaluate(this));
    }

    @Override
    public int interpret(BinaryOperationExpression expression) {
        BinaryOperationExpression.Type type = expression.type();
        int left = expression.left().evaluate(this);
        int right = expression.right().evaluate(this);

        if(expression.type() == BinaryOperationExpression.Type.ASSIGNMENT) {
            if (expression.left() instanceof IdentifierExpression identifierExpression) {
                assign(identifierExpression.getIdentifier().getData(), right);
            } else {
                throw new RuntimeException("Left hand of an assignment must be an identifier");
            }
        }

        return getBinaryOperator(type).applyAsInt(left, right);
    }

    @Override
    public int interpret(EnclosedExpression expression) {
        return expression.expression().evaluate(this);
    }

    @Override
    public int interpret(FunctionCallExpression expression) {
        Token identifier = expression.identifier();
        String name = identifier.getData();
        List<Expression> arguments = expression.arguments();

        //check if the function is a library function
        Optional<ToIntFunction<List<Integer>>> libraryFunction = getLibraryFunction(name);

        if(libraryFunction.isPresent()) {
            return libraryFunction.get().applyAsInt(arguments.stream().map(arg -> arg.evaluate(this)).collect(Collectors.toList()));
        }

        //get the function being called
        Function function = getFunction(name);

        //get the functions parameters and verify argument number
        List<Token> parameters = function.parameters();
        if(arguments.size() != parameters.size()) {
            throw new InterpreterRuntimeException("Expected " + parameters.size() + " parameters, Given " + arguments.size(), expression);
        }

        //enter function scope
        enterFunctionScope();

        //add each argument to the context
        for(int i = 0; i < arguments.size(); i++) {
            addVariable(parameters.get(i).getData(), arguments.get(i).evaluate(this));
        }

        function.body().interpret(this);

        int returnValue = getReturnValue();

        exitScope();

        return returnValue;
    }

    @Override
    public int interpret(IdentifierExpression expression) {
        return getVariable(expression.identifier().getData());
    }

    @Override
    public int interpret(LiteralExpression expression) {
        return Integer.parseInt(expression.literal().getData());
    }

    @Override
    public int interpret(UnaryOperationExpression expression) {
        UnaryOperationExpression.Type type = expression.type();
        int operand = expression.operand().evaluate(this);

        return getUnaryOperator(type).applyAsInt(operand);
    }
}
