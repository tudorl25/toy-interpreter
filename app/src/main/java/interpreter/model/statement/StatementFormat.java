package interpreter.model.statement;

/** Helper for toString: a compound statement nested inside another statement is shown in parentheses. */
final class StatementFormat {
    private StatementFormat() {
    }

    static String nested(IStatement stmt) {
        return stmt instanceof ComparisonStatement ? "(" + stmt + ")" : String.valueOf(stmt);
    }
}
