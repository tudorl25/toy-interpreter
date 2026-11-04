package interpreter.model.statement;

import interpreter.model.PrgState;


public record CompoundStatement(IStatement first, IStatement second) implements IStatement {
    @Override
    public PrgState execute(PrgState state) {
        state.getExeStack().push(second);
        state.getExeStack().push(first);
        return state;
    }

    @Override
    public String toString() {
        return StatementFormat.nested(first) + "; " + StatementFormat.nested(second);
    }
}
