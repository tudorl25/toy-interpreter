package interpreter.model.exp;

import static org.junit.jupiter.api.Assertions.*;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyHeap;
import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReadHeapExpTest {
    private IDictionary<String, IValue> symTable;
    private IHeap heap;

    @BeforeEach
    void setUp() {
        symTable = new MyDictionary<>();
        heap = new MyHeap();
    }

    @Test
    void readsValueAndNestedReferenceFromHeap() throws MyException {
        int addr1 = heap.allocate(new IntValue(20));
        int addr2 = heap.allocate(new RefValue(addr1, new IntType()));
        symTable.put("v", new RefValue(addr1, new IntType()));
        symTable.put("a", new RefValue(addr2, new RefType(new IntType())));

        assertEquals(new IntValue(20), new ReadHeapExp(new VarExp("v")).eval(symTable, heap));
        assertEquals(new IntValue(20), new ReadHeapExp(new ReadHeapExp(new VarExp("a"))).eval(symTable, heap));
    }

    @Test
    void nonReferenceExpressionThrows() {
        IExp exp = new ReadHeapExp(new ValueExp(new IntValue(5)));
        assertThrows(ExpressionException.class, () -> exp.eval(symTable, heap));
    }

    @Test
    void addressNotInHeapThrows() {
        symTable.put("v", new RefValue(0, new IntType())); // null address
        IExp expNull = new ReadHeapExp(new VarExp("v"));
        assertThrows(ExpressionException.class, () -> expNull.eval(symTable, heap));

        IExp expMissing = new ReadHeapExp(new ValueExp(new RefValue(99, new IntType())));
        var ex = assertThrows(ExpressionException.class, () -> expMissing.eval(symTable, heap));
        assertEquals("rH: address 99 is not in the heap", ex.getMessage());
    }
}