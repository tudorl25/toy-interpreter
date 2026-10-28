package interpreter.model.statement;

import interpreter.model.ProgramState;


public record CompoundStatement(IStatement first, IStatement second) implements IStatement {
    @Override
    public ProgramState execute(ProgramState state) {
        state.getExeStack().push(second);
        state.getExeStack().push(first);
        return state;
    }

    @Override
    public String toString() {
        return StatementFormat.nested(first) + "; " + StatementFormat.nested(second);
    }
}
