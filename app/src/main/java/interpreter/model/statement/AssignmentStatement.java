package interpreter.model.statement;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.expression.IExpression;
import interpreter.model.value.IValue;

/** Rule S2: Id = Exp -> evaluate Exp and update Id in the symbol table. */
public record AssignmentStatement(String id, IExpression exp) implements IStatement {
    @Override
    public PrgState execute(PrgState state) throws MyException {
        var symTable = state.getSymTable();
        if (!symTable.isDefined(id))
            throw new StatementException("variable " + id + " was not declared");
        IValue val = exp.eval(symTable);
        if (!val.getType().equals(symTable.lookup(id).getType()))
            throw new StatementException("declared type of variable " + id
                    + " and type of the assigned expression do not match");
        symTable.update(id, val);
        return state;
    }

    @Override
    public String toString() {
        return id + " = " + exp;
    }
}
