package ru.otus.atm.commands;

// Решил, что достаточно будет команд только на undo
public interface Command {
    void undo();
}
