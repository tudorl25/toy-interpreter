package interpreter.controller;

import interpreter.model.value.IValue;
import interpreter.model.value.RefValue;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class GarbageCollector {
    private GarbageCollector() {}

    /** The addresses that occur in the RefValues of the SymTable. */
    public static Set<Integer> getAddrFromSymTable(Collection<IValue> symTableValues) {
        return symTableValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> ((RefValue) v).address())
                .collect(Collectors.toSet());
    }

    /** UNSAFE: keeps only the heap entries referred to directly from the SymTable. */
    public static Map<Integer, IValue> unsafeGarbageCollector(Set<Integer> symTableAddr,
                                                              Map<Integer, IValue> heap) {
        return heap.entrySet().stream()
                .filter(e -> symTableAddr.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /** Computes all heap addresses reachable from the given root addresses, directly or via the heap. */
    public static Set<Integer> reachableAddresses(Set<Integer> roots, Map<Integer, IValue> heap) {
        Set<Integer> visited = new HashSet<>();
        Deque<Integer> workList = new ArrayDeque<>();

        for (int addr : roots) {
            if (heap.containsKey(addr) && visited.add(addr)) {
                workList.add(addr);
            }
        }

        while (!workList.isEmpty()) {
            int current = workList.poll();
            if (heap.get(current) instanceof RefValue ref) {
                int nextAddr = ref.address();
                if (heap.containsKey(nextAddr) && visited.add(nextAddr)) {
                    workList.add(nextAddr);
                }
            }
        }
        return visited;
    }

    /** SAFE: keeps all heap entries reachable directly from the SymTable or transitively through the heap. */
    public static Map<Integer, IValue> safeGarbageCollector(Set<Integer> symTableAddr,
                                                            Map<Integer, IValue> heap) {
        Set<Integer> reachable = reachableAddresses(symTableAddr, heap);
        return heap.entrySet().stream()
                .filter(e -> reachable.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}