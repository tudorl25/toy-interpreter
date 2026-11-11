package interpreter.view;

import static org.junit.jupiter.api.Assertions.*;

import interpreter.controller.Controller;
import interpreter.model.PrgState;
import interpreter.repository.Repository;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class TextMenuTest {
    @TempDir
    Path dir;

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUpStreams() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    void restoreStreams() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    @Test
    void runExampleCommandExecutesOnceAndRejectsSecondRun() {
        TextMenu menu = new TextMenu();
        var repo = new Repository(PrgState.of(Examples.EX5), dir.resolve("log5.txt").toString());
        var controller = new Controller(repo, false);
        menu.addCommand(new RunExampleCommand("5", Examples.EX5.toString(), controller));

        // Run command "5" twice, plus an invalid option "99"
        String input = "99\n5\n5\n";
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        menu.show();

        String output = outContent.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("Invalid option"));
        assertTrue(output.contains("This example has already been executed."));
        assertEquals(1, repo.getCrtPrg().getOut().size());
    }
}