package interpreter.controller;

import static org.junit.jupiter.api.Assertions.*;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.stmt.IStmt;
import interpreter.model.stmt.NopStmt;
import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import interpreter.repository.Repository;
import interpreter.view.Examples;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ControllerTest {
    @TempDir
    Path dir;

    private PrgState run(IStmt program) throws MyException {
        PrgState state = PrgState.of(program);
        var repo = new Repository(state, dir.resolve("log.txt").toString());
        new Controller(repo, false).allStep();
        return state;
    }

    @Test
    void example1PrintsTwo() throws MyException {
        PrgState state = run(Examples.EX1);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(List.of(new IntValue(2)), state.getOut().toList());
    }

    @Test
    void example2PrintsEighteen() throws MyException {
        PrgState state = run(Examples.EX2);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(List.of(new IntValue(18)), state.getOut().toList());
    }

    @Test
    void example3PrintsTwo() throws MyException {
        PrgState state = run(Examples.EX3);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(List.of(new IntValue(2)), state.getOut().toList());
    }

    @Test
    void fileExampleReadsBothNumbersAndClosesTheFile() throws Exception {
        Path in = dir.resolve("test.in");
        Files.writeString(in, "15\n50\n");

        PrgState s = run(Examples.fileExample(in.toString()));
        assertEquals(List.of(new IntValue(15), new IntValue(50)), s.getOut().toList());
        assertTrue(s.getFileTable().toMap().isEmpty());
    }

    @Test
    void fileExampleWithEmptyFilePrintsZeroZero() throws Exception {
        Path in = dir.resolve("empty.in");
        Files.writeString(in, "");

        PrgState s = run(Examples.fileExample(in.toString()));
        assertEquals(List.of(new IntValue(0), new IntValue(0)), s.getOut().toList());
        assertTrue(s.getFileTable().toMap().isEmpty());
    }

    @Test
    void example5PrintsFive() throws MyException {
        PrgState state = run(Examples.EX5);
        assertEquals(List.of(new IntValue(5)), state.getOut().toList());
    }

    @Test
    void example6AllocatesNestedRefsAndPrintsThem() throws MyException {
        PrgState s = run(Examples.EX6);
        RefValue expectedV = new RefValue(1, new IntType());
        RefValue expectedA = new RefValue(2, new RefType(new IntType()));
        assertEquals(List.of(expectedV, expectedA), s.getOut().toList());
        assertEquals(new IntValue(20), s.getHeap().get(1));
        assertEquals(expectedV, s.getHeap().get(2));
    }

    @Test
    void example7ReadsHeapAndPrintsTwentyAndTwentyFive() throws MyException {
        PrgState s = run(Examples.EX7);
        assertEquals(List.of(new IntValue(20), new IntValue(25)), s.getOut().toList());
    }

    @Test
    void example8WritesHeapAndPrintsTwentyAndThirtyFive() throws MyException {
        PrgState s = run(Examples.EX8);
        assertEquals(List.of(new IntValue(20), new IntValue(35)), s.getOut().toList());
        assertEquals(new IntValue(30), s.getHeap().get(1));
    }

    @Test
    void example9PrintsTwentyWithSafeGarbageCollector() throws MyException {
        PrgState s = run(Examples.EX9);
        assertEquals(List.of(new IntValue(20)), s.getOut().toList());
    }

    @Test
    void example10WhileLoopPrintsFourDownToZero() throws MyException {
        PrgState s = run(Examples.EX10);
        assertEquals(
                List.of(new IntValue(4), new IntValue(3), new IntValue(2), new IntValue(1), new IntValue(0)),
                s.getOut().toList()
        );
    }

    @Test
    void logFileContainsAllFiveSectionsInOrder() throws Exception {
        run(Examples.EX6);
        String log = Files.readString(dir.resolve("log.txt"));
        int a = log.indexOf("ExeStack:");
        int b = log.indexOf("SymTable:");
        int c = log.indexOf("Out:");
        int d = log.indexOf("FileTable:");
        int e = log.indexOf("Heap:");
        assertTrue(0 <= a && a < b && b < c && c < d && d < e);
    }

    @Test
    void oneStepOnEmptyStackThrows() throws MyException {
        PrgState state = run(new NopStmt());
        var repo = new Repository(state, dir.resolve("log.txt").toString());
        Controller controller = new Controller(repo, false);
        var ex = assertThrows(StatementException.class, () -> controller.oneStep(state));
        assertEquals("execution stack is empty", ex.getMessage());
    }
}