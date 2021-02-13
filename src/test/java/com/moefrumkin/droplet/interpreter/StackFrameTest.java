package com.moefrumkin.droplet.interpreter;

import com.moefrumkin.droplet.interpreter.basicInterpreter.stackframe.FunctionStackFrame;
import com.moefrumkin.droplet.interpreter.basicInterpreter.stackframe.GlobalStackFrame;
import com.moefrumkin.droplet.interpreter.basicInterpreter.stackframe.SimpleStackFrame;
import com.moefrumkin.droplet.interpreter.basicInterpreter.stackframe.StackFrame;
import com.moefrumkin.droplet.parser.Parser;
import com.moefrumkin.droplet.parser.exception.UnexpectedTokenException;
import com.moefrumkin.droplet.parser.exception.UnexpectedTokenTypeException;
import com.moefrumkin.droplet.parser.function.Function;
import com.moefrumkin.droplet.token.Token;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StackFrameTest {
    GlobalStackFrame main;
    Function noOp;
    StackFrame ifFrame;
    FunctionStackFrame noOpFrame;

    void initData() throws UnexpectedTokenTypeException, UnexpectedTokenException {
        main = new GlobalStackFrame();
        noOp = Function.parse(new Parser(Token.tokenize("def noOp(){}")));
        main.addFunction(noOp);
        ifFrame = new SimpleStackFrame(main);
        noOpFrame = new FunctionStackFrame(ifFrame, 0);
    }

    @Test
    public void testEquality() throws UnexpectedTokenTypeException, UnexpectedTokenException {
        initData();

        GlobalStackFrame mainCopy = new GlobalStackFrame();
        mainCopy.addFunction(noOp);

        assertEquals(mainCopy, main);
        assertNotEquals(new GlobalStackFrame(), main);
        assertEquals(new SimpleStackFrame(main), ifFrame);
        assertNotEquals(new SimpleStackFrame(new GlobalStackFrame()), ifFrame);
        assertNotEquals(main, ifFrame);

        main.returnFunction();

        assertNotEquals(new GlobalStackFrame(), main);

        assertEquals(main, ifFrame.getSuper());
        assertEquals(main, noOpFrame.getSuper().getSuper());
    }

    @Test
    public void testVariables() throws UnexpectedTokenTypeException, UnexpectedTokenException {
        initData();

        main.addVariable("foo", 98);

        assertEquals(98, main.getVariable("foo"));
        assertEquals(98, ifFrame.getVariable("foo"));
        assertEquals(98, noOpFrame.getVariable("foo"));

        noOpFrame.setVariable("foo", 49);

        assertEquals(49, main.getVariable("foo"));
        assertEquals(49, ifFrame.getVariable("foo"));
        assertEquals(49, noOpFrame.getVariable("foo"));

        ifFrame.addVariable("foo", 25);

        assertEquals(49, main.getVariable("foo"));
        assertEquals(25, ifFrame.getVariable("foo"));
        assertEquals(25, noOpFrame.getVariable("foo"));
    }

    @Test
    public void testGetFunction() throws UnexpectedTokenTypeException, UnexpectedTokenException {
        initData();

        assertEquals(main.getFunction("noOp"), noOp);
        assertEquals(ifFrame.getFunction("noOp"), noOp);
        assertEquals(noOpFrame.getFunction("noOp"), noOp);
    }

    @Test
    public void testFrameReturn() throws UnexpectedTokenTypeException, UnexpectedTokenException {
        initData();

        assertFalse(main.getToReturn());
        assertEquals(0, main.getReturnValue());
        assertFalse(ifFrame.getToReturn());

        ifFrame.returnFunction(42);

        assertTrue(main.getToReturn());
        assertEquals(42, main.getReturnValue());
        assertTrue(ifFrame.getToReturn());
    }

}
