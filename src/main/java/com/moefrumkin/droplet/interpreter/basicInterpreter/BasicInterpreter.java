package com.moefrumkin.droplet.interpreter.basicInterpreter;

import com.moefrumkin.droplet.interpreter.basicInterpreter.stackframe.FunctionStackFrame;
import com.moefrumkin.droplet.interpreter.basicInterpreter.exceptions.InterpreterOutputException;
import com.moefrumkin.droplet.interpreter.basicInterpreter.stackframe.GlobalStackFrame;
import com.moefrumkin.droplet.interpreter.basicInterpreter.stackframe.SimpleStackFrame;
import com.moefrumkin.droplet.interpreter.basicInterpreter.stackframe.StackFrame;
import com.moefrumkin.droplet.parser.expression.BinaryOperationExpression;
import com.moefrumkin.droplet.parser.expression.UnaryOperationExpression;
import com.moefrumkin.droplet.parser.function.Function;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.*;

/**
 * An interpreter that performs no optimizations and literally interprets the program.
 */
public class BasicInterpreter extends AbstractInterpreter {

    private static final int DEFAULT_RETURN = 0;
    private static final IntPredicate DEFAULT_TRUTHINESS = i -> i != 0;
    private static final Comparator<Integer> DEFAULT_ORDERING = Comparator.comparingInt(a -> a);
    private static final int DEFAULT_FALSE = 0;
    private static final int DEFAULT_TRUE = 1;
    private static final Map<BinaryOperationExpression.Type, IntBinaryOperator> DEFAULT_BINARY_OPERATIONS = Map.ofEntries(
            Map.entry(BinaryOperationExpression.Type.ADDITION, Integer::sum),
            Map.entry(BinaryOperationExpression.Type.SUBTRACTION, (a, b) -> a - b),
            Map.entry(BinaryOperationExpression.Type.MULTIPLICATION, (a, b) -> a * b),
            Map.entry(BinaryOperationExpression.Type.OR, (a, b) -> DEFAULT_TRUTHINESS.test(a) || DEFAULT_TRUTHINESS.test(b) ? DEFAULT_TRUE : DEFAULT_FALSE),
            Map.entry(BinaryOperationExpression.Type.AND, (a, b) -> DEFAULT_TRUTHINESS.test(a) && DEFAULT_TRUTHINESS.test(b) ? DEFAULT_TRUE : DEFAULT_FALSE),
            Map.entry(BinaryOperationExpression.Type.EQUALS, (a, b) -> a == b ? DEFAULT_TRUE : DEFAULT_FALSE),
            Map.entry(BinaryOperationExpression.Type.NOTEQUALS, (a, b) -> a != b ? DEFAULT_TRUE : DEFAULT_FALSE),
            Map.entry(BinaryOperationExpression.Type.GREATER, (a, b) -> DEFAULT_ORDERING.compare(a, b) > 0 ? DEFAULT_TRUE : DEFAULT_FALSE),
            Map.entry(BinaryOperationExpression.Type.LESS, (a, b) -> DEFAULT_ORDERING.compare(a, b) < 0 ? DEFAULT_TRUE : DEFAULT_FALSE)
    );
    private static final Map<UnaryOperationExpression.Type, IntUnaryOperator> DEFAULT_UNARY_OPERATIONS = Map.ofEntries(
            Map.entry(UnaryOperationExpression.Type.NEGATION, i -> -i),
            Map.entry(UnaryOperationExpression.Type.BOOLEAN_NEGATION, i -> DEFAULT_TRUTHINESS.test(i) ? DEFAULT_TRUE : DEFAULT_FALSE)
    );

    private StackFrame frame;
    private OutputStream output;
    private static Map<String, ToIntFunction<List<Integer>>> functionLibrary;

    public BasicInterpreter() {
        frame = new GlobalStackFrame();
        output = System.out;

        functionLibrary = Map.ofEntries(
                Map.entry("print", args -> {
                    args.forEach(this::print);
                    return DEFAULT_RETURN;
                }),
                Map.entry("println", args -> {
                    args.forEach(arg -> print(arg, '\n'));
                    return DEFAULT_RETURN;
                })
        );
    }

    public BasicInterpreter(OutputStream output) {
        this();
        this.output = output;
    }

    private void pushFrame() {
        this.frame = new SimpleStackFrame(this.frame);
    }

    private void pushFunctionFrame() {
        this.frame = new FunctionStackFrame(this.frame, DEFAULT_RETURN);
    }

    private void popFrame() {
        frame = frame.getSuper();
    }

    private void print(int i) {
        print(i, null);
    }

    private void print(int i, Character delimiter) {
        try {
            output.write(String.valueOf(i).getBytes());
            if(delimiter != null)
                output.write(delimiter);
        } catch (IOException e) {
            throw new InterpreterOutputException("An error occurred when trying to print", e, frame);
        }
    }

    @Override
    public boolean truthy(int value) {
        return value != 0;
    }

    @Override
    public void addVariable(String name, int value) {
        frame.addVariable(name, value);
    }

    @Override
    public int getVariable(String name) {
        return frame.getVariable(name);
    }

    @Override
    public void addFunction(Function function) {
        frame.addFunction(function);
    }

    @Override
    public Function getFunction(String name) {
        return frame.getFunction(name);
    }

    @Override
    public IntBinaryOperator getBinaryOperator(BinaryOperationExpression.Type type) {
        return DEFAULT_BINARY_OPERATIONS.get(type);
    }

    @Override
    public IntUnaryOperator getUnaryOperator(UnaryOperationExpression.Type type) {
        return DEFAULT_UNARY_OPERATIONS.get(type);
    }

    @Override
    public void assign(String name, int value) {
        frame.setVariable(name, value);
    }

    @Override
    public void enterScope() {
        pushFrame();
    }

    @Override
    public void enterFunctionScope() {
        pushFunctionFrame();
    }

    @Override
    public void exitScope() {
        popFrame();
    }

    @Override
    public void returnValue(int value) {
        frame.returnFunction(value);
    }

    @Override
    public int getReturnValue() {
        return frame.getReturnValue();
    }

    @Override
    public boolean getToReturn() {
        return frame.getToReturn();
    }

    @Override
    public Optional<ToIntFunction<List<Integer>>> getLibraryFunction(String name) {
        return Optional.ofNullable(functionLibrary.get(name));
    }
}
