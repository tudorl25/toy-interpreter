package interpreter.view;

import interpreter.model.expression.ArithmeticExpression;
import interpreter.model.expression.ArithmeticOperand;
import interpreter.model.expression.ValueExpression;
import interpreter.model.expression.VariableExpression;
import interpreter.model.statement.AssignmentStatement;
import interpreter.model.statement.ComparisonStatement;
import interpreter.model.statement.IStatement;
import interpreter.model.statement.IfStatement;
import interpreter.model.statement.PrintStatement;
import interpreter.model.statement.VaraibleDeclarationStatement;
import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;

import java.util.List;

public final class Examples {
    private Examples() {
    }

    // Ex1: int v; v = 2; print(v)
    public static final IStatement EX1 = new ComparisonStatement(new VaraibleDeclarationStatement("v", new IntType()),
            new ComparisonStatement(new AssignmentStatement("v", new ValueExpression(new IntValue(2))),
                    new PrintStatement(new VariableExpression("v"))));

    // Ex2: int a; int b; a = 2 + 3 * 5; b = a + 1; print(b)
    public static final IStatement EX2 = new ComparisonStatement(new VaraibleDeclarationStatement("a", new IntType()),
            new ComparisonStatement(new VaraibleDeclarationStatement("b", new IntType()),
                    new ComparisonStatement(new AssignmentStatement("a",
                            new ArithmeticExpression(ArithmeticOperand.ADD, new ValueExpression(new IntValue(2)),
                                    new ArithmeticExpression(ArithmeticOperand.MUL, new ValueExpression(new IntValue(3)),
                                            new ValueExpression(new IntValue(5))))),
                            new ComparisonStatement(new AssignmentStatement("b",
                                    new ArithmeticExpression(ArithmeticOperand.ADD, new VariableExpression("a"),
                                            new ValueExpression(new IntValue(1)))),
                                    new PrintStatement(new VariableExpression("b"))))));

    // Ex3: bool a; int v; a = true; (if a then v = 2 else v = 3); print(v)
    public static final IStatement EX3 = new ComparisonStatement(new VaraibleDeclarationStatement("a", new BoolType()),
            new ComparisonStatement(new VaraibleDeclarationStatement("v", new IntType()),
                    new ComparisonStatement(new AssignmentStatement("a", new ValueExpression(new BoolValue(true))),
                            new ComparisonStatement(new IfStatement(new VariableExpression("a"),
                                    new AssignmentStatement("v", new ValueExpression(new IntValue(2))),
                                    new AssignmentStatement("v", new ValueExpression(new IntValue(3)))),
                                    new PrintStatement(new VariableExpression("v"))))));

    public static List<IStatement> all() {
        return List.of(EX1, EX2, EX3);
    }
}
