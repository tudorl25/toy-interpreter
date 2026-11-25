package interpreter.model.type;

import static org.junit.jupiter.api.Assertions.*;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.stmt.VarDeclStmt;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import interpreter.model.value.StringValue;
import org.junit.jupiter.api.Test;

class TypeAndValueTest {
    @Test
    void defaultValuesMatchSpecification() {
        assertEquals(new IntValue(0), new IntType().defaultValue());
        assertEquals(new BoolValue(false), new BoolType().defaultValue());
        assertEquals(new StringValue(""), new StringType().defaultValue());
        assertEquals(new RefValue(0, new IntType()), new RefType(new IntType()).defaultValue());
        assertEquals(
                new RefValue(0, new RefType(new IntType())),
                new RefType(new RefType(new IntType())).defaultValue()
        );
    }

    @Test
    void refVariableGetsNullDefaultValueOnDeclaration() throws MyException {
        PrgState state = PrgState.of(new VarDeclStmt("v", new RefType(new IntType())));
        state.getExeStack().pop().execute(state);
        assertEquals(new RefValue(0, new IntType()), state.getSymTable().lookup("v"));
    }

    @Test
    void stringVariableGetsEmptyStringDefaultValueOnDeclaration() throws MyException {
        PrgState state = PrgState.of(new VarDeclStmt("s", new StringType()));
        state.getExeStack().pop().execute(state);
        assertEquals(new StringValue(""), state.getSymTable().lookup("s"));
    }

    @Test
    void nestedRefTypesCompareByInnerType() {
        IType refInt = new RefType(new IntType());
        IType refRefInt1 = new RefType(new RefType(new IntType()));
        IType refRefInt2 = new RefType(new RefType(new IntType()));
        IType refRefBool = new RefType(new RefType(new BoolType()));

        assertEquals(refRefInt1, refRefInt2);
        assertEquals(refRefInt1.hashCode(), refRefInt2.hashCode());
        assertNotEquals(refRefInt1, refInt);
        assertNotEquals(refRefInt1, refRefBool);
        assertEquals(refInt, new RefValue(1, new IntType()).getType());
        assertEquals("(1,int)", new RefValue(1, new IntType()).toString());
        assertEquals("Ref(Ref(int))", refRefInt1.toString());
    }

    @Test
    void stringValueEqualsAndHashCodeCompareByContent() {
        StringValue s1 = new StringValue(new String("test.in"));
        StringValue s2 = new StringValue(new String("test.in"));
        StringValue s3 = new StringValue("other.in");

        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
        assertNotEquals(s1, s3);
        assertEquals("\"test.in\"", s1.toString());
    }
}