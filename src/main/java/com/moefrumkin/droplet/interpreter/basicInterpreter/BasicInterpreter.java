package com.moefrumkin.droplet.interpreter.basicInterpreter;

import com.moefrumkin.droplet.interpreter.basicInterpreter.exceptions.InterpreterOutputException;
import com.moefrumkin.droplet.interpreter.basicInterpreter.stackframe.FunctionStackFrame;
import com.moefrumkin.droplet.interpreter.basicInterpreter.stackframe.GlobalStackFrame;
import com.moefrumkin.droplet.interpreter.basicInterpreter.stackframe.SimpleStackFrame;
import com.moefrumkin.droplet.interpreter.basicInterpreter.stackframe.StackFrame;
import com.moefrumkin.droplet.parser.expression.BinaryOperationExpression;
import com.moefrumkin.droplet.parser.expression.UnaryOperationExpression;
import com.moefrumkin.droplet.parser.function.Function;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
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
    private static final OutputStream DEFAULT_OUTPUT = System.out;
    private static final Map<String, ToIntBiFunction<BasicInterpreter, List<Integer>>> DEFAULT_LIBRARY = Map.ofEntries(
            Map.entry("print", (interpreter, args) -> { args.forEach(interpreter::print); return interpreter.returnValue; }),
            Map.entry("println", (interpreter, args) -> { args.forEach(arg -> interpreter.print(arg,  '\n')); return interpreter.returnValue; })
    );

    private final int returnValue;
    private final IntPredicate truthiness;
    private final Map<BinaryOperationExpression.Type, IntBinaryOperator> binaryOperations;
    private final Map<UnaryOperationExpression.Type, IntUnaryOperator> unaryOperations;
    private final OutputStream output;
    private final Map<String, ToIntBiFunction<BasicInterpreter, List<Integer>>> functions;

    private StackFrame frame;

    private BasicInterpreter(Builder builder) {
        this.returnValue = builder.returnValue;
        this.truthiness = builder.truthiness;
        this.binaryOperations = builder.binaryOperations;
        this.unaryOperations = builder.unaryOperations;
        this.output = builder.output;
        this.functions = builder.functions;

        frame = new GlobalStackFrame();
    }



    public static final class Builder {
        private int returnValue = DEFAULT_RETURN;
        private IntPredicate truthiness = DEFAULT_TRUTHINESS;
        private Map<BinaryOperationExpression.Type, IntBinaryOperator> binaryOperations = DEFAULT_BINARY_OPERATIONS;
        private Map<UnaryOperationExpression.Type, IntUnaryOperator> unaryOperations = DEFAULT_UNARY_OPERATIONS;
        private OutputStream output = DEFAULT_OUTPUT;
        private final Map<String, ToIntBiFunction<BasicInterpreter, List<Integer>>> functions = new HashMap<>(DEFAULT_LIBRARY);

        public Builder() {}

        public Builder returnValue(int newReturn) { returnValue = newReturn; return this; }
        public Builder truthiness(IntPredicate newTruthiness) { truthiness = newTruthiness; return this; }
        public Builder binaryOperations(Map<BinaryOperationExpression.Type, IntBinaryOperator> newOperations) { binaryOperations = newOperations; return this; }
        public Builder unaryOperations(Map<UnaryOperationExpression.Type, IntUnaryOperator> newOperations) { this.unaryOperations = newOperations; return this; }
        public Builder output(OutputStream newOutput) { this.output = newOutput; return this; }
        public Builder addFunction(String name, ToIntBiFunction<BasicInterpreter, List<Integer>> function) { functions.put(name, function); return this; }

        public BasicInterpreter build() { return new BasicInterpreter(this); }
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
        return truthiness.test(value);
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
        return binaryOperations.get(type);
    }

    @Override
    public IntUnaryOperator getUnaryOperator(UnaryOperationExpression.Type type) {
        return unaryOperations.get(type);
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
        return Optional.ofNullable(functions.get(name)).map(function -> (args -> function.applyAsInt(this, args)));
    }
}
