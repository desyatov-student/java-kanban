package ru.praktikum.kanban.util;

public class IdentifierGenerator {

    public static final int INITIAL_IDENTIFIER = 1;
    private Integer currentId;

    public IdentifierGenerator() {
        this.currentId = INITIAL_IDENTIFIER;
    }

    public Integer getNextId() {
        Integer nextId = currentId;
        currentId++;
        return nextId;
    }
}
