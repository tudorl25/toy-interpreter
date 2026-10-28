package interpreter.model.expression;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

import static interpreter.TestSupport.bool;
import static interpreter.TestSupport.num;
import static org.junit.jupiter.api.Assertions.*;

class ArithmeticExpressionTest {
    private final IDictionary<String, IValue> symTable = new MyDictionary<>();

    @Test
    void addition() throws MyException {
        assertEquals(new IntValue(10), new ArithmeticExpression(ArithmeticOperand.ADD, num(7), num(3)).eval(symTable));
    }

    @Test
    void subtraction() throws MyException {
        assertEquals(new IntValue(4), new ArithmeticExpression(ArithmeticOperand.SUB, num(7), num(3)).eval(symTable));
    }

    @Test
    void subtractionCanBeNegative() throws MyException {
        assertEquals(new IntValue(-4), new ArithmeticExpression(ArithmeticOperand.SUB, num(3), num(7)).eval(symTable));
    }

    @Test
    void multiplication() throws MyException {
        assertEquals(new IntValue(21), new ArithmeticExpression(ArithmeticOperand.MUL, num(7), num(3)).eval(symTable));
    }

    @Test
    void divisionIsIntegerDivision() throws MyException {
        assertEquals(new IntValue(2), new ArithmeticExpression(ArithmeticOperand.DIV, num(7), num(3)).eval(symTable));
    }

    @Test
    void exactDivision() throws MyException {
        assertEquals(new IntValue(5), new ArithmeticExpression(ArithmeticOperand.DIV, num(10), num(2)).eval(symTable));
    }

    @Test
    void respectsOperatorPrecedenceEncodedInTheTree() throws MyException {
        IExpression e = new ArithmeticExpression(ArithmeticOperand.ADD, num(2), new ArithmeticExpression(ArithmeticOperand.MUL, num(3), num(5)));
        assertEquals(new IntValue(17), e.eval(symTable));
    }

    @Test
    void divisionByZeroThrows() {
        IExpression e = new ArithmeticExpression(ArithmeticOperand.DIV, num(1), num(0));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable));
        assertEquals("division by zero", ex.getMessage());
    }

    @Test
    void zeroDividedByANumberIsFine() throws MyException {
        assertEquals(new IntValue(0), new ArithmeticExpression(ArithmeticOperand.DIV, num(0), num(5)).eval(symTable));
    }

    @Test
    void booleanFirstOperandThrows() {
        IExpression e = new ArithmeticExpression(ArithmeticOperand.ADD, bool(true), num(1));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable));
        assertEquals("first operand is not an integer", ex.getMessage());
    }

    @Test
    void booleanSecondOperandThrows() {
        IExpression e = new ArithmeticExpression(ArithmeticOperand.MUL, num(1), bool(false));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable));
        assertEquals("second operand is not an integer", ex.getMessage());
    }

    @Test
    void errorInANestedOperandIsPropagated() {
        IExpression e = new ArithmeticExpression(ArithmeticOperand.ADD, num(1), new ArithmeticExpression(ArithmeticOperand.DIV, num(1), num(0)));
        assertThrows(ExpressionException.class, () -> e.eval(symTable));
    }

    @Test
    void undefinedVariableOperandThrows() {
        IExpression e = new ArithmeticExpression(ArithmeticOperand.ADD, new VariableExpression("x"), num(1));
        assertThrows(ExpressionException.class, () -> e.eval(symTable));
    }

    @Test
    void variableOperandsAreLookedUp() throws MyException {
        symTable.put("a", new IntValue(5));
        IExpression e = new ArithmeticExpression(ArithmeticOperand.ADD, new VariableExpression("a"), num(1));
        assertEquals(new IntValue(6), e.eval(symTable));
    }

    @Test
    void toStringIsFullyParenthesised() {
        IExpression e = new ArithmeticExpression(ArithmeticOperand.ADD, num(2), new ArithmeticExpression(ArithmeticOperand.MUL, num(3), num(5)));
        assertEquals("(2 + (3 * 5))", e.toString());
    }
}
