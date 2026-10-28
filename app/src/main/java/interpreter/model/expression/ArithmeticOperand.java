package interpreter.model.expression;

public enum ArithmeticOperand {
    ADD("+"), SUB("-"), MUL("*"), DIV("/");

    private final String symbol;

    ArithmeticOperand(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
