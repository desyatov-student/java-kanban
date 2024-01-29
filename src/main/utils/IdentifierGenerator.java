package main.utils;

public class IdentifierGenerator {

    public final static int INITIAL_IDENTIFIER = 1;
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
