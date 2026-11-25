package interpreter.model.stmt;

import static org.junit.jupiter.api.Assertions.*;

import interpreter.controller.Controller;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ArithExp;
import interpreter.model.exp.ArithOp;
import interpreter.model.exp.RelOp;
import interpreter.model.exp.RelationalExp;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import interpreter.repository.Repository;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class HeapStmtTest {
    @TempDir
    Path dir;

    private PrgState state;

    @BeforeEach
    void setup() throws MyException {
        state = PrgState.of(new NopStmt());
        state.getExeStack().pop();
        new VarDeclStmt("v", new RefType(new IntType())).execute(state);
    }

    @Test
    void newAllocatesAtAddressOneAndUpdatesTheVariable() throws MyException {
        new NewStmt("v", new ValueExp(new IntValue(20))).execute(state);
        assertEquals(new RefValue(1, new IntType()), state.getSymTable().lookup("v"));
        assertEquals(new IntValue(20), state.getHeap().get(1));
    }

    @Test
    void newErrorsThrowStatementException() throws MyException {
        // Variable not declared
        assertThrows(StatementException.class,
                () -> new NewStmt("missing", new ValueExp(new IntValue(1))).execute(state));

        // Variable is not a reference
        new VarDeclStmt("x", new IntType()).execute(state);
        assertThrows(StatementException.class,
                () -> new NewStmt("x", new ValueExp(new IntValue(1))).execute(state));

        // Value of wrong type
        var wrongTypeExp = new ValueExp(new BoolValue(true));
        assertThrows(StatementException.class,
                () -> new NewStmt("v", wrongTypeExp).execute(state));
    }

    @Test
    void writeHeapUpdatesExistingLocation() throws MyException {
        new NewStmt("v", new ValueExp(new IntValue(20))).execute(state);
        new WriteHeapStmt("v", new ValueExp(new IntValue(30))).execute(state);
        assertEquals(new IntValue(30), state.getHeap().get(1));
    }

    @Test
    void writeHeapErrorsThrowStatementException() throws MyException {
        // Variable not declared
        assertThrows(StatementException.class,
                () -> new WriteHeapStmt("missing", new ValueExp(new IntValue(1))).execute(state));

        // Variable is not a reference
        new VarDeclStmt("x", new IntType()).execute(state);
        assertThrows(StatementException.class,
                () -> new WriteHeapStmt("x", new ValueExp(new IntValue(1))).execute(state));

        // Address not in heap (v is initially (0, int))
        assertThrows(StatementException.class,
                () -> new WriteHeapStmt("v", new ValueExp(new IntValue(1))).execute(state));

        // Value of wrong type
        new NewStmt("v", new ValueExp(new IntValue(20))).execute(state);
        assertThrows(StatementException.class,
                () -> new WriteHeapStmt("v", new ValueExp(new BoolValue(false))).execute(state));
    }

    @Test
    void whileWithANonBooleanConditionThrows() {
        var loop = new WhileStmt(new ValueExp(new IntValue(1)), new NopStmt());
        assertThrows(StatementException.class, () -> loop.execute(state));
    }

    private PrgState runLoopWithStart(int start) throws MyException {
        IStmt prg = new CompStmt(
                new VarDeclStmt("i", new IntType()),
                new CompStmt(
                        new AssignStmt("i", new ValueExp(new IntValue(start))),
                        new WhileStmt(
                                new RelationalExp(RelOp.GT, new VarExp("i"), new ValueExp(new IntValue(0))),
                                new CompStmt(
                                        new PrintStmt(new VarExp("i")),
                                        new AssignStmt("i", new ArithExp(ArithOp.SUB,
                                                new VarExp("i"), new ValueExp(new IntValue(1))))
                                )
                        )
                )
        );
        PrgState s = PrgState.of(prg);
        new Controller(new Repository(s, dir.resolve("loop.txt").toString()), false).allStep();
        return s;
    }

    @Test
    void whileRunsZeroOneAndSeveralTimes() throws MyException {
        assertEquals(List.of(), runLoopWithStart(0).getOut().toList());
        assertEquals(List.of(new IntValue(1)), runLoopWithStart(1).getOut().toList());
        assertEquals(
                List.of(new IntValue(3), new IntValue(2), new IntValue(1)),
                runLoopWithStart(3).getOut().toList()
        );
    }
}