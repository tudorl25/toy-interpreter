package interpreter.model.statement;

import interpreter.model.PrgState;


public record NopStatement() implements IStatement {
    @Override
    public PrgState execute(PrgState state) {
        return state;
    }

    @Override
    public String toString() {
        return "nop";
    }
}
