package interpreter.repository;

import interpreter.model.ProgramState;

public interface IRepository {
    ProgramState getCurrentProgram();
}
