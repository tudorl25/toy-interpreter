package interpreter.repository;

import static org.junit.jupiter.api.Assertions.*;

import interpreter.model.PrgState;
import interpreter.view.Examples;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class RepositoryTest {
    @TempDir
    Path dir;

    @Test
    void getCrtPrgAndLogPrgStateExecIncludeHeapSection() throws Exception {
        Path logPath = dir.resolve("repo-log.txt");
        PrgState state = PrgState.of(Examples.EX6);
        IRepository repo = new Repository(state, logPath.toString());

        assertSame(state, repo.getCrtPrg());
        repo.logPrgStateExec();

        String content = Files.readString(logPath);
        assertTrue(content.contains("ExeStack:"));
        assertTrue(content.contains("SymTable:"));
        assertTrue(content.contains("Out:"));
        assertTrue(content.contains("FileTable:"));
        assertTrue(content.contains("Heap:"));
    }
}