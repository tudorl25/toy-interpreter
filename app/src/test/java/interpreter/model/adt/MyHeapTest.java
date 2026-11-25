package interpreter.model.adt;

import static org.junit.jupiter.api.Assertions.*;

import interpreter.exceptions.ADTException;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MyHeapTest {
    private IHeap heap;

    @BeforeEach
    void setUp() {
        heap = new MyHeap();
    }

    @Test
    void allocateReturnsSequentialAddressesStartingAtOne() throws ADTException {
        assertEquals(1, heap.allocate(new IntValue(10)));
        assertEquals(2, heap.allocate(new IntValue(20)));
        assertEquals(3, heap.allocate(new IntValue(30)));

        assertTrue(heap.isDefined(1));
        assertFalse(heap.isDefined(0));
        assertEquals(new IntValue(10), heap.get(1));
        assertEquals(new IntValue(20), heap.get(2));
        assertEquals(new IntValue(30), heap.get(3));
    }

    @Test
    void updateModifiesExistingAddress() throws ADTException {
        int addr = heap.allocate(new IntValue(10));
        heap.update(addr, new IntValue(99));
        assertEquals(new IntValue(99), heap.get(addr));
    }

    @Test
    void getAndUpdateMissingAddressThrowADTException() {
        assertThrows(ADTException.class, () -> heap.get(0));
        assertThrows(ADTException.class, () -> heap.get(5));
        assertThrows(ADTException.class, () -> heap.update(5, new IntValue(1)));
    }

    @Test
    void getContentIsReadOnlyAndSetContentReplacesMap() {
        heap.allocate(new IntValue(10));
        Map<Integer, IValue> content = heap.getContent();
        assertThrows(UnsupportedOperationException.class, () -> content.put(99, new IntValue(1)));

        heap.setContent(Map.of(2, new IntValue(42)));
        assertFalse(heap.isDefined(1));
        assertTrue(heap.isDefined(2));
    }
}