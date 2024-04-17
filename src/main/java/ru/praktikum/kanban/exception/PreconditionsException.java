package ru.praktikum.kanban.exception;

public class PreconditionsException extends RuntimeException {
    public PreconditionsException(String message) {
        super(message);
    }
}
