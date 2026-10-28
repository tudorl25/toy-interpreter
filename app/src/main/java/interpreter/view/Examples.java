package interpreter.view;

import interpreter.model.expression.ArithmeticExpression;
import interpreter.model.expression.ArithmeticOperand;
import interpreter.model.expression.ValueExpression;
import interpreter.model.expression.VariableExpression;
import interpreter.model.statement.AssignmentStatement;
import interpreter.model.statement.CompoundStatement;
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

    
    public static final IStatement EX1 = new CompoundStatement(new VaraibleDeclarationStatement("v", new IntType()),
            new CompoundStatement(new AssignmentStatement("v", new ValueExpression(new IntValue(2))),
                    new PrintStatement(new VariableExpression("v"))));

    
    public static final IStatement EX2 = new CompoundStatement(new VaraibleDeclarationStatement("a", new IntType()),
            new CompoundStatement(new VaraibleDeclarationStatement("b", new IntType()),
                    new CompoundStatement(new AssignmentStatement("a",
                            new ArithmeticExpression(ArithmeticOperand.ADD, new ValueExpression(new IntValue(2)),
                                    new ArithmeticExpression(ArithmeticOperand.MUL, new ValueExpression(new IntValue(3)),
                                            new ValueExpression(new IntValue(5))))),
                            new CompoundStatement(new AssignmentStatement("b",
                                    new ArithmeticExpression(ArithmeticOperand.ADD, new VariableExpression("a"),
                                            new ValueExpression(new IntValue(1)))),
                                    new PrintStatement(new VariableExpression("b"))))));

    
    public static final IStatement EX3 = new CompoundStatement(new VaraibleDeclarationStatement("a", new BoolType()),
            new CompoundStatement(new VaraibleDeclarationStatement("v", new IntType()),
                    new CompoundStatement(new AssignmentStatement("a", new ValueExpression(new BoolValue(true))),
                            new CompoundStatement(new IfStatement(new VariableExpression("a"),
                                    new AssignmentStatement("v", new ValueExpression(new IntValue(2))),
                                    new AssignmentStatement("v", new ValueExpression(new IntValue(3)))),
                                    new PrintStatement(new VariableExpression("v"))))));

    public static List<IStatement> all() {
        return List.of(EX1, EX2, EX3);
    }
}
