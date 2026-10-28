package interpreter.model.statement;

import interpreter.exceptions.MyException;
import interpreter.model.ProgramState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static interpreter.TestSupport.emptyState;
import static interpreter.TestSupport.num;
import static org.junit.jupiter.api.Assertions.*;

class CompoundStatementTest {
    private ProgramState state;

    @BeforeEach
    void setUp() {
        state = emptyState();
    }

    @Test
    void pushesTheSecondStatementThenTheFirstSoTheFirstIsOnTop() throws MyException {
        IStatement first = new NopStatement();
        IStatement second = new PrintStatement(num(1));
        new CompoundStatement(first, second).execute(state);
        assertSame(first, state.getExeStack().pop());
        assertSame(second, state.getExeStack().pop());
        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void doesNotChangeSymTableOrOut() throws MyException {
        new CompoundStatement(new NopStatement(), new NopStatement()).execute(state);
        assertEquals("{}", state.getSymTable().toString());
        assertEquals(0, state.getOut().size());
    }

    @Test
    void toStringHasNoOuterParenthesesButNestedCompoundsHaveThem() {
        IStatement s = new CompoundStatement(new NopStatement(), new CompoundStatement(new NopStatement(), new NopStatement()));
        assertEquals("nop; (nop; nop)", s.toString());
    }
}
