package interpreter.model.statement;

import interpreter.exceptions.MyException;
import interpreter.model.ProgramState;
import interpreter.model.expression.IExpression;


public record PrintStatement(IExpression exp) implements IStatement {
    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        state.getOut().add(exp.eval(state.getSymTable()));
        return state;
    }

    @Override
    public String toString() {
        return "print(" + exp + ")";
    }
}
