package interpreter.model.expression;

public enum LogicOperand {
    AND("and"), OR("or");

    private final String symbol;

    LogicOperand(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
