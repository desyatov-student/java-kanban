package ru.praktikum.kanban.util;

public class IdentifierGenerator {

    public static final int INITIAL_IDENTIFIER = 1;
    private int currentId;

    public IdentifierGenerator() {
        this.currentId = INITIAL_IDENTIFIER;
    }

    public int getNextId() {
        int nextId = currentId;
        currentId++;
        return nextId;
    }
}
