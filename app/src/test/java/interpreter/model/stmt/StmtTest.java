package interpreter.model.stmt;

import static org.junit.jupiter.api.Assertions.*;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

class StmtTest {
    private static PrgState stateWith(IStmt stmt) {
        return PrgState.of(stmt);
    }

    @Test
    void compStmtPushesSecondThenFirst() throws MyException {
        IStmt s1 = new NopStmt();
        IStmt s2 = new PrintStmt(new ValueExp(new IntValue(1)));
        PrgState state = stateWith(new CompStmt(s1, s2));

        IStmt top = state.getExeStack().pop();
        top.execute(state);

        assertEquals(s1, state.getExeStack().pop());
        assertEquals(s2, state.getExeStack().pop());
        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void varDeclStmtAddsDefaultValueAndRejectsDuplicate() throws MyException {
        IStmt decl = new VarDeclStmt("v", new IntType());
        PrgState state = stateWith(decl);
        state.getExeStack().pop().execute(state);

        assertEquals(new IntValue(0), state.getSymTable().lookup("v"));
        assertThrows(StatementException.class, () -> decl.execute(state));
    }

    @Test
    void assignStmtUpdatesDefinedVariableAndChecksErrors() throws MyException {
        PrgState state = stateWith(new NopStmt());
        state.getExeStack().pop();

        IStmt assignUndeclared = new AssignStmt("x", new ValueExp(new IntValue(5)));
        assertThrows(StatementException.class, () -> assignUndeclared.execute(state));

        new VarDeclStmt("x", new IntType()).execute(state);

        IStmt assignWrongType = new AssignStmt("x", new ValueExp(new BoolValue(true)));
        assertThrows(StatementException.class, () -> assignWrongType.execute(state));

        new AssignStmt("x", new ValueExp(new IntValue(12))).execute(state);
        assertEquals(new IntValue(12), state.getSymTable().lookup("x"));
    }

    @Test
    void printStmtAppendsEvaluatedExpressionToOut() throws MyException {
        PrgState state = stateWith(new PrintStmt(new ValueExp(new IntValue(42))));
        state.getExeStack().pop().execute(state);

        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(42), state.getOut().get(0));
    }

    @Test
    void ifStmtPushesCorrectBranchAndChecksBooleanCondition() throws MyException {
        IStmt thenBranch = new PrintStmt(new ValueExp(new IntValue(1)));
        IStmt elseBranch = new PrintStmt(new ValueExp(new IntValue(2)));

        PrgState trueState = stateWith(new IfStmt(new ValueExp(new BoolValue(true)), thenBranch, elseBranch));
        trueState.getExeStack().pop().execute(trueState);
        assertEquals(thenBranch, trueState.getExeStack().pop());

        PrgState falseState = stateWith(new IfStmt(new ValueExp(new BoolValue(false)), thenBranch, elseBranch));
        falseState.getExeStack().pop().execute(falseState);
        assertEquals(elseBranch, falseState.getExeStack().pop());

        PrgState badCondState = stateWith(new IfStmt(new ValueExp(new IntValue(0)), thenBranch, elseBranch));
        IStmt badIf = badCondState.getExeStack().pop();
        assertThrows(StatementException.class, () -> badIf.execute(badCondState));
    }

    @Test
    void nopStmtLeavesStateUnchanged() throws MyException {
        PrgState state = stateWith(new NopStmt());
        state.getExeStack().pop().execute(state);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(0, state.getOut().size());
    }

    @Test
    void varExpInsidePrintStmtWorks() throws MyException {
        PrgState state = stateWith(new NopStmt());
        state.getExeStack().pop();
        new VarDeclStmt("v", new IntType()).execute(state);
        new AssignStmt("v", new ValueExp(new IntValue(9))).execute(state);
        new PrintStmt(new VarExp("v")).execute(state);
        assertEquals(new IntValue(9), state.getOut().get(0));
    }
}
