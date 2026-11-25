package interpreter.controller;

import static org.junit.jupiter.api.Assertions.*;

import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

class GarbageCollectorTest {
    // Ex9 state after new(v, 30): v -> (3, int), a -> (2, Ref(int))
    private final List<IValue> ex9SymTable = List.of(
            new RefValue(3, new IntType()),
            new RefValue(2, new RefType(new IntType()))
    );
    private final Map<Integer, IValue> ex9Heap = Map.of(
            1, new IntValue(20),
            2, new RefValue(1, new IntType()),
            3, new IntValue(30),
            4, new IntValue(7)
    );

    @Test
    void safeCollectorKeepsLocationsReachableThroughTheHeapAndDropsGarbage() {
        Set<Integer> roots = GarbageCollector.getAddrFromSymTable(ex9SymTable);
        Map<Integer, IValue> kept = GarbageCollector.safeGarbageCollector(roots, ex9Heap);
        assertEquals(Set.of(1, 2, 3), kept.keySet());
    }

    @Test
    void unsafeCollectorDropsIndirectlyReachableAddress() {
        Set<Integer> roots = GarbageCollector.getAddrFromSymTable(ex9SymTable);
        Map<Integer, IValue> kept = GarbageCollector.unsafeGarbageCollector(roots, ex9Heap);
        assertEquals(Set.of(2, 3), kept.keySet());
    }

    @Test
    void lab8Exercise2KeepsAddressesTwoAndThreeOnly() {
        // SymTable: a -> (3, Ref(int)), c -> (0, int)
        List<IValue> symTable = List.of(
                new RefValue(3, new RefType(new IntType())),
                new RefValue(0, new IntType())
        );
        // Heap: 1 -> 7, 2 -> 8, 3 -> (2, int), 4 -> (1, int), 5 -> (4, Ref(int))
        Map<Integer, IValue> heap = Map.of(
                1, new IntValue(7),
                2, new IntValue(8),
                3, new RefValue(2, new IntType()),
                4, new RefValue(1, new IntType()),
                5, new RefValue(4, new RefType(new IntType()))
        );

        Set<Integer> roots = GarbageCollector.getAddrFromSymTable(symTable);
        assertEquals(Set.of(2, 3), GarbageCollector.reachableAddresses(roots, heap));
        assertEquals(Set.of(2, 3), GarbageCollector.safeGarbageCollector(roots, heap).keySet());
        assertEquals(Set.of(3), GarbageCollector.unsafeGarbageCollector(roots, heap).keySet());
    }

    @Test
    void reachableAddressesTerminatesEvenOnCyclicHeap() {
        Map<Integer, IValue> cyclicHeap = Map.of(
                1, new RefValue(2, new IntType()),
                2, new RefValue(1, new IntType()),
                3, new IntValue(99)
        );
        assertEquals(Set.of(1, 2), GarbageCollector.reachableAddresses(Set.of(1), cyclicHeap));
    }
}