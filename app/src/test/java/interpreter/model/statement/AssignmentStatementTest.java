package interpreter.model.statement;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.expression.ArithmeticExpression;
import interpreter.model.expression.ArithmeticOperand;
import interpreter.model.expression.VariableExpression;
import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static interpreter.TestSupport.bool;
import static interpreter.TestSupport.emptyState;
import static interpreter.TestSupport.num;
import static org.junit.jupiter.api.Assertions.*;

class AssignmentStatementTest {
    private PrgState state;

    @BeforeEach
    void setUp() throws MyException {
        state = emptyState();
        new VaraibleDeclarationStatement("v", new IntType()).execute(state);
        new VaraibleDeclarationStatement("b", new BoolType()).execute(state);
    }

    @Test
    void assignsTheValueOfTheExpression() throws MyException {
        new AssignmentStatement("v", num(2)).execute(state);
        assertEquals(new IntValue(2), state.getSymTable().lookup("v"));
    }

    @Test
    void assignsABooleanToABooleanVariable() throws MyException {
        new AssignmentStatement("b", bool(true)).execute(state);
        assertEquals(new BoolValue(true), state.getSymTable().lookup("b"));
    }

    @Test
    void expressionIsEvaluatedAgainstTheCurrentSymTable() throws MyException {
        new AssignmentStatement("v", num(4)).execute(state);
        new AssignmentStatement("v", new ArithmeticExpression(ArithmeticOperand.ADD, new VariableExpression("v"), num(1))).execute(state);
        assertEquals(new IntValue(5), state.getSymTable().lookup("v"));
    }

    @Test
    void assignmentToAnUndeclaredVariableThrows() {
        var ex = assertThrows(StatementException.class, () -> new AssignmentStatement("x", num(1)).execute(state));
        assertEquals("variable x was not declared", ex.getMessage());
    }

    @Test
    void assigningABooleanToAnIntVariableThrows() {
        assertThrows(StatementException.class, () -> new AssignmentStatement("v", bool(true)).execute(state));
    }

    @Test
    void assigningAnIntegerToABooleanVariableThrows() {
        assertThrows(StatementException.class, () -> new AssignmentStatement("b", num(1)).execute(state));
    }

    @Test
    void typeMismatchLeavesTheVariableUnchanged() throws MyException {
        new AssignmentStatement("v", num(3)).execute(state);
        assertThrows(StatementException.class, () -> new AssignmentStatement("v", bool(true)).execute(state));
        assertEquals(new IntValue(3), state.getSymTable().lookup("v"));
    }

    @Test
    void errorOfTheExpressionIsPropagated() {
        assertThrows(ExpressionException.class, () -> new AssignmentStatement("v", new VariableExpression("undefined")).execute(state));
    }

    @Test
    void toStringShowsTheAssignment() {
        assertEquals("v = 2", new AssignmentStatement("v", num(2)).toString());
    }
}
