package main.utils;

public class IdentifierGenerator {

    final static int INITIAL_IDENTIFIER = 1;
    private int currentId;

    public IdentifierGenerator() {
        this.currentId = INITIAL_IDENTIFIER;
    }

    public int getNextId() {
        currentId++;
        return currentId;
    }
}
