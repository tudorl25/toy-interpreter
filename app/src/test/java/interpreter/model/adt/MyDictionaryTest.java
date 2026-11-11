package interpreter.model.adt;

import static org.junit.jupiter.api.Assertions.*;

import interpreter.exceptions.ADTException;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MyDictionaryTest {
    private IDictionary<String, Integer> dict;

    @BeforeEach
    void setUp() {
        dict = new MyDictionary<>();
    }

    @Test
    void putLookupAndUpdateWorkCorrectly() throws ADTException {
        assertFalse(dict.isDefined("x"));
        dict.put("x", 5);
        assertTrue(dict.isDefined("x"));
        assertEquals(5, dict.lookup("x"));

        dict.update("x", 42);
        assertEquals(42, dict.lookup("x"));
    }

    @Test
    void removeDeletesKeyAndReturnsValue() throws ADTException {
        dict.put("k", 99);
        dict.remove("k");
        assertFalse(dict.isDefined("k"));
    }

    @Test
    void removeMissingKeyThrows() {
        assertThrows(ADTException.class, () -> dict.remove("missing"));
    }

    @Test
    void toMapReturnsUnmodifiableViewOfEntries() {
        dict.put("a", 1);
        dict.put("b", 2);
        Map<String, Integer> map = dict.toMap();

        assertEquals(Map.of("a", 1, "b", 2), map);
        assertThrows(UnsupportedOperationException.class, () -> map.put("c", 3));
    }

    @Test
    void lookupMissingKeyThrows() {
        assertThrows(ADTException.class, () -> dict.lookup("missing"));
    }

    @Test
    void updateMissingKeyThrows() {
        assertThrows(ADTException.class, () -> dict.update("missing", 1));
    }
}
