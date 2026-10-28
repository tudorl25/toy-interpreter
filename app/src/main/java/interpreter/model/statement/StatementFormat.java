package interpreter.model.statement;

final class StatementFormat {
    private StatementFormat() {
    }

    static String nested(IStatement stmt) {
        return stmt instanceof CompoundStatement ? "(" + stmt + ")" : String.valueOf(stmt);
    }
}
