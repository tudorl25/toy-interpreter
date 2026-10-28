package interpreter.repository;

import interpreter.model.ProgramState;

import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository {
    private final List<ProgramState> states = new ArrayList<>();

    public Repository(ProgramState initialState) {
        states.add(initialState);
    }

    @Override
    public ProgramState getCurrentProgram() {
        return states.get(0);
    }
}
