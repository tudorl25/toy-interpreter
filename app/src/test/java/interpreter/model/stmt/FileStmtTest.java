package interpreter.model.stmt;

import static org.junit.jupiter.api.Assertions.*;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileStmtTest {
    @TempDir
    Path dir;

    private PrgState state;
    private ValueExp file;
    private StringValue fileNameVal;

    @BeforeEach
    void setup() throws Exception {
        Path f = dir.resolve("numbers.in");
        Files.writeString(f, "7\n");
        fileNameVal = new StringValue(f.toString());
        file = new ValueExp(fileNameVal);
        state = PrgState.of(new NopStmt());
        new VarDeclStmt("x", new IntType()).execute(state);
    }

    @Test
    void openRFileAddsFileToFileTableAndCloseRemovesIt() throws MyException {
        new OpenRFileStmt(file).execute(state);
        assertTrue(state.getFileTable().isDefined(fileNameVal));

        new CloseRFileStmt(file).execute(state);
        assertFalse(state.getFileTable().isDefined(fileNameVal));
    }

    @Test
    void readGivesTheNextNumberThenZeroAtTheEnd() throws MyException {
        new OpenRFileStmt(file).execute(state);
        new ReadFileStmt(file, "x").execute(state);
        assertEquals(new IntValue(7), state.getSymTable().lookup("x"));

        new ReadFileStmt(file, "x").execute(state);
        assertEquals(new IntValue(0), state.getSymTable().lookup("x"));
        new CloseRFileStmt(file).execute(state);
    }

    @Test
    void openingTheSameFileTwiceThrows() throws MyException {
        new OpenRFileStmt(file).execute(state);
        assertThrows(StatementException.class,
                () -> new OpenRFileStmt(file).execute(state));
        new CloseRFileStmt(file).execute(state);
    }

    @Test
    void openRFileWithNonStringExpressionThrows() {
        assertThrows(StatementException.class,
                () -> new OpenRFileStmt(new ValueExp(new IntValue(1))).execute(state));
    }

    @Test
    void openRFileWithMissingFileThrows() {
        ValueExp missing = new ValueExp(new StringValue(dir.resolve("missing.in").toString()));
        assertThrows(StatementException.class,
                () -> new OpenRFileStmt(missing).execute(state));
    }

    @Test
    void readFileErrorsThrowStatementException() throws Exception {
        // Undeclared variable
        assertThrows(StatementException.class,
                () -> new ReadFileStmt(file, "undeclared").execute(state));

        // Non-int variable
        new VarDeclStmt("flag", new BoolType()).execute(state);
        assertThrows(StatementException.class,
                () -> new ReadFileStmt(file, "flag").execute(state));

        // Non-string file expression
        assertThrows(StatementException.class,
                () -> new ReadFileStmt(new ValueExp(new IntValue(1)), "x").execute(state));

        // File not open
        assertThrows(StatementException.class,
                () -> new ReadFileStmt(file, "x").execute(state));

        // Non-integer line in file
        Path badFile = dir.resolve("bad.in");
        Files.writeString(badFile, "not_an_int\n");
        ValueExp badExp = new ValueExp(new StringValue(badFile.toString()));
        new OpenRFileStmt(badExp).execute(state);
        assertThrows(StatementException.class,
                () -> new ReadFileStmt(badExp, "x").execute(state));
        new CloseRFileStmt(badExp).execute(state);
    }

    @Test
    void closeRFileErrorsThrowStatementException() {
        // Non-string expression
        assertThrows(StatementException.class,
                () -> new CloseRFileStmt(new ValueExp(new IntValue(1))).execute(state));

        // Closing a file that is not open
        assertThrows(StatementException.class,
                () -> new CloseRFileStmt(file).execute(state));
    }
}