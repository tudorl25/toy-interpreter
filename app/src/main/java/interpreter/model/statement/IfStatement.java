package interpreter.model.statement;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.expression.IExpression;
import interpreter.model.value.BoolValue;

public record IfStatement(IExpression cond, IStatement thenS, IStatement elseS) implements IStatement {
    @Override
    public PrgState execute(PrgState state) throws MyException {
        if (!(cond.eval(state.getSymTable()) instanceof BoolValue(boolean c)))
            throw new StatementException("condition of the if statement is not a boolean");
        state.getExeStack().push(c ? thenS : elseS);
        return state;
    }

    @Override
    public String toString() {
        return "(if " + cond + " then " + StatementFormat.nested(thenS) + " else " + StatementFormat.nested(elseS) + ")";
    }
}
