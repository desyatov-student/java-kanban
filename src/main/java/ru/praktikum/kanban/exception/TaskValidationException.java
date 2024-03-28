package ru.praktikum.kanban.exception;

public class TaskValidationException extends Exception {
    public TaskValidationException(String message) {
        super(message);
    }

    public TaskValidationException() {
    }
}
