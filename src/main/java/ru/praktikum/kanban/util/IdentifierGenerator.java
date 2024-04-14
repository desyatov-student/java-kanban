package ru.praktikum.kanban.util;

public class IdentifierGenerator {

    public static final Integer INITIAL_IDENTIFIER = 1;
    private Integer currentId;

    public IdentifierGenerator() {
        this.currentId = INITIAL_IDENTIFIER;
    }

    public IdentifierGenerator(Integer initialId) {
        this.currentId = initialId;
    }

    public Integer getNextId() {
        Integer nextId = currentId;
        currentId++;
        return nextId;
    }
}
