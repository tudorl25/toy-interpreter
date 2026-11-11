package interpreter.view;

import static org.junit.jupiter.api.Assertions.*;

import interpreter.model.stmt.IStmt;
import java.util.List;
import org.junit.jupiter.api.Test;

class ExamplesTest {
    @Test
    void allReturnsFiveExamplesInOrder() {
        List<IStmt> examples = Examples.all();
        assertEquals(5, examples.size());
        assertSame(Examples.EX1, examples.get(0));
        assertSame(Examples.EX2, examples.get(1));
        assertSame(Examples.EX3, examples.get(2));
        assertSame(Examples.EX4, examples.get(3));
        assertSame(Examples.EX5, examples.get(4));
    }
}