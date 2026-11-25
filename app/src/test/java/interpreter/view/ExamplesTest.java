package interpreter.view;

import static org.junit.jupiter.api.Assertions.*;

import interpreter.model.stmt.IStmt;
import java.util.List;
import org.junit.jupiter.api.Test;

class ExamplesTest {
    @Test
    void allReturnsTenExamplesInOrder() {
        List<IStmt> examples = Examples.all();
        assertEquals(10, examples.size());
        assertSame(Examples.EX1, examples.get(0));
        assertSame(Examples.EX6, examples.get(5));
        assertSame(Examples.EX9, examples.get(8));
        assertSame(Examples.EX10, examples.get(9));
    }
}