package interpreter.model;

import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IList;
import interpreter.model.adt.IStack;
import interpreter.model.statement.IStatement;
import interpreter.model.value.IValue;

/** The state of one executed program: ExeStack, SymTable and Out (plus the original program). */
public class PrgState {
    private IStack<IStatement> exeStack;
    private IDictionary<String, IValue> symTable;
    private IList<IValue> out;
    private final IStatement originalProgram; // statements are immutable records, so no deep copy is needed

    /** Initially the execution stack contains the whole program; SymTable and Out are as given (empty). */
    public PrgState(IStack<IStatement> exeStack, IDictionary<String, IValue> symTable,
                    IList<IValue> out, IStatement program) {
        this.exeStack = exeStack;
        this.symTable = symTable;
        this.out = out;
        this.originalProgram = program;
        exeStack.push(program);
    }

    public IStack<IStatement> getExeStack() {
        return exeStack;
    }

    public void setExeStack(IStack<IStatement> exeStack) {
        this.exeStack = exeStack;
    }

    public IDictionary<String, IValue> getSymTable() {
        return symTable;
    }

    public void setSymTable(IDictionary<String, IValue> symTable) {
        this.symTable = symTable;
    }

    public IList<IValue> getOut() {
        return out;
    }

    public void setOut(IList<IValue> out) {
        this.out = out;
    }

    public IStatement getOriginalProgram() {
        return originalProgram;
    }

    @Override
    public String toString() {
        return "ExeStack: " + exeStack + "\nSymTable: " + symTable + "\nOut: " + out;
    }
}
