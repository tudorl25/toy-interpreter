package interpreter.model.expression;

import interpreter.model.adt.IDictionary;
import interpreter.model.value.IValue;

/** Rule E1: a constant evaluates to itself. */
public record ValueExpression(IValue value) implements IExpression {
    @Override
    public IValue eval(IDictionary<String, IValue> symTable) {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
