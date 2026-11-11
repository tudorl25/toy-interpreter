package interpreter.model.exp;

import static org.junit.jupiter.api.Assertions.*;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RelationalExpTest {
    private IDictionary<String, IValue> symTable;

    private static IExp num(int n) {
        return new ValueExp(new IntValue(n));
    }

    @BeforeEach
    void setUp() {
        symTable = new MyDictionary<>();
    }

    @Test
    void lessThanTrueAndFalse() throws MyException {
        assertEquals(new BoolValue(true), new RelationalExp(RelOp.LT, num(2), num(5)).eval(symTable));
        assertEquals(new BoolValue(false), new RelationalExp(RelOp.LT, num(5), num(5)).eval(symTable));
    }

    @Test
    void lessOrEqualTrueAndFalse() throws MyException {
        assertEquals(new BoolValue(true), new RelationalExp(RelOp.LE, num(5), num(5)).eval(symTable));
        assertEquals(new BoolValue(true), new RelationalExp(RelOp.LE, num(3), num(5)).eval(symTable));
        assertEquals(new BoolValue(false), new RelationalExp(RelOp.LE, num(6), num(5)).eval(symTable));
    }

    @Test
    void equalTrueAndFalse() throws MyException {
        assertEquals(new BoolValue(true), new RelationalExp(RelOp.EQ, num(4), num(4)).eval(symTable));
        assertEquals(new BoolValue(false), new RelationalExp(RelOp.EQ, num(4), num(5)).eval(symTable));
    }

    @Test
    void notEqualTrueAndFalse() throws MyException {
        assertEquals(new BoolValue(true), new RelationalExp(RelOp.NE, num(4), num(5)).eval(symTable));
        assertEquals(new BoolValue(false), new RelationalExp(RelOp.NE, num(4), num(4)).eval(symTable));
    }

    @Test
    void greaterThanTrueAndFalse() throws MyException {
        assertEquals(new BoolValue(true), new RelationalExp(RelOp.GT, num(7), num(3)).eval(symTable));
        assertEquals(new BoolValue(false), new RelationalExp(RelOp.GT, num(3), num(3)).eval(symTable));
    }

    @Test
    void greaterOrEqualTrueAndFalse() throws MyException {
        assertEquals(new BoolValue(true), new RelationalExp(RelOp.GE, num(3), num(3)).eval(symTable));
        assertEquals(new BoolValue(true), new RelationalExp(RelOp.GE, num(5), num(3)).eval(symTable));
        assertEquals(new BoolValue(false), new RelationalExp(RelOp.GE, num(2), num(3)).eval(symTable));
    }

    @Test
    void nonIntegerFirstOperandThrows() {
        IExp exp = new RelationalExp(RelOp.LT, new ValueExp(new BoolValue(true)), num(5));
        var ex = assertThrows(ExpressionException.class, () -> exp.eval(symTable));
        assertEquals("first operand is not an integer", ex.getMessage());
    }

    @Test
    void nonIntegerSecondOperandThrows() {
        IExp exp = new RelationalExp(RelOp.EQ, num(5), new ValueExp(new StringValue("5")));
        var ex = assertThrows(ExpressionException.class, () -> exp.eval(symTable));
        assertEquals("second operand is not an integer", ex.getMessage());
    }
}
