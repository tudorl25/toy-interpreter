package interpreter.model.statement;

import interpreter.exceptions.MyException;
import interpreter.model.ProgramState;
import org.junit.jupiter.api.Test;

import static interpreter.TestSupport.emptyState;
import static org.junit.jupiter.api.Assertions.*;

class NopStmtTest {
    @Test
    void changesNothingInTheState() throws MyException {
        ProgramState state = emptyState();
        ProgramState result = new NopStatement().execute(state);
        assertSame(state, result);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals("{}", state.getSymTable().toString());
        assertEquals(0, state.getOut().size());
    }

    @Test
    void toStringIsNop() {
        assertEquals("nop", new NopStatement().toString());
    }
}
