package interpreter.model.statement;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.ProgramState;
import interpreter.model.type.IType;

public record VaraibleDeclarationStatement(String name, IType type) implements IStatement {
    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        var symTable = state.getSymTable();
        if (symTable.isDefined(name))
            throw new StatementException("variable " + name + " is already declared");
        symTable.put(name, type.defaultValue());
        return state;
    }

    @Override
    public String toString() {
        return type + " " + name;
    }
}
