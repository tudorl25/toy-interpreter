package interpreter.model.type;

import static org.junit.jupiter.api.Assertions.*;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.stmt.VarDeclStmt;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;
import org.junit.jupiter.api.Test;

class TypeAndValueTest {
    @Test
    void defaultValuesMatchSpecification() {
        assertEquals(new IntValue(0), new IntType().defaultValue());
        assertEquals(new BoolValue(false), new BoolType().defaultValue());
        assertEquals(new StringValue(""), new StringType().defaultValue());
    }

    @Test
    void stringVariableGetsEmptyStringDefaultValueOnDeclaration() throws MyException {
        PrgState state = PrgState.of(new VarDeclStmt("s", new StringType()));
        state.getExeStack().pop().execute(state);
        assertEquals(new StringValue(""), state.getSymTable().lookup("s"));
    }

    @Test
    void typeEqualityAndValueGetTypeWorkCorrectly() {
        assertEquals(new IntType(), new IntValue(10).getType());
        assertEquals(new BoolType(), new BoolValue(true).getType());
        assertEquals(new StringType(), new StringValue("abc").getType());
        assertNotEquals(new IntType(), new BoolType());
        assertNotEquals(new IntType(), new StringType());
    }

    @Test
    void stringValueEqualsAndHashCodeWorkByValue() {
        StringValue s1 = new StringValue(new String("test.in"));
        StringValue s2 = new StringValue(new String("test.in"));
        StringValue s3 = new StringValue("other.in");

        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
        assertNotEquals(s1, s3);
    }
}
