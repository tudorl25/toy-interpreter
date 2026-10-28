package interpreter.controller;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.ProgramState;
import interpreter.model.statement.IStatement;
import interpreter.repository.IRepository;

import java.io.PrintStream;

public class Controller {
    private final IRepository repo;
    private final boolean displayFlag;
    private final PrintStream display;

    public Controller(IRepository repo, boolean displayFlag) {
        this(repo, displayFlag, System.out);
    }

    public Controller(IRepository repo, boolean displayFlag, PrintStream display) {
        this.repo = repo;
        this.displayFlag = displayFlag;
        this.display = display;
    }

    public ProgramState oneStep(ProgramState state) throws MyException {
        var stack = state.getExeStack();
        if (stack.isEmpty()) throw new StatementException("execution stack is empty");
        IStatement crtStmt = stack.pop();
        return crtStmt.execute(state);
    }

    public void allStep() throws MyException {
        ProgramState prg = repo.getCurrentProgram();
        display(prg); // only if the display flag is on
        while (!prg.getExeStack().isEmpty()) {
            oneStep(prg);
            display(prg);
        }
    }

    private void display(ProgramState state) {
        if (displayFlag) {
            display.println(state);
            display.println();
        }
    }
}
