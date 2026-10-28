package interpreter.model.statement;

import interpreter.model.ProgramState;


public record NopStatement() implements IStatement {
    @Override
    public ProgramState execute(ProgramState state) {
        return state;
    }

    @Override
    public String toString() {
        return "nop";
    }
}
