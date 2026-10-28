package interpreter.model.expression;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import org.junit.jupiter.api.Test;

import static interpreter.TestSupport.bool;
import static interpreter.TestSupport.num;
import static org.junit.jupiter.api.Assertions.*;

class LogicExpTest {
    private final IDictionary<String, IValue> symTable = new MyDictionary<>();

    private IValue eval(LogicOperand op, boolean a, boolean b) throws MyException {
        return new LogicExpression(op, bool(a), bool(b)).eval(symTable);
    }

    @Test
    void andTruthTable() throws MyException {
        assertEquals(new BoolValue(true), eval(LogicOperand.AND, true, true));
        assertEquals(new BoolValue(false), eval(LogicOperand.AND, true, false));
        assertEquals(new BoolValue(false), eval(LogicOperand.AND, false, true));
        assertEquals(new BoolValue(false), eval(LogicOperand.AND, false, false));
    }

    @Test
    void orTruthTable() throws MyException {
        assertEquals(new BoolValue(true), eval(LogicOperand.OR, true, true));
        assertEquals(new BoolValue(true), eval(LogicOperand.OR, true, false));
        assertEquals(new BoolValue(true), eval(LogicOperand.OR, false, true));
        assertEquals(new BoolValue(false), eval(LogicOperand.OR, false, false));
    }

    @Test
    void integerFirstOperandThrows() {
        IExpression e = new LogicExpression(LogicOperand.AND, num(1), bool(true));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable));
        assertEquals("first operand is not a boolean", ex.getMessage());
    }

    @Test
    void integerSecondOperandThrows() {
        IExpression e = new LogicExpression(LogicOperand.OR, bool(true), num(1));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable));
        assertEquals("second operand is not a boolean", ex.getMessage());
    }

    @Test
    void bothOperandsAreEvaluatedEvenIfTheLeftOneDecidesTheResult() {
        IExpression e = new LogicExpression(LogicOperand.OR, bool(true), new VariableExpression("undefined"));
        assertThrows(ExpressionException.class, () -> e.eval(symTable));
    }

    @Test
    void variableOperandsAreLookedUp() throws MyException {
        symTable.put("a", new BoolValue(true));
        IExpression e = new LogicExpression(LogicOperand.AND, new VariableExpression("a"), bool(true));
        assertEquals(new BoolValue(true), e.eval(symTable));
    }

    @Test
    void toStringShowsTheKeyword() {
        assertEquals("(true and false)", new LogicExpression(LogicOperand.AND, bool(true), bool(false)).toString());
        assertEquals("(true or false)", new LogicExpression(LogicOperand.OR, bool(true), bool(false)).toString());
    }
}
