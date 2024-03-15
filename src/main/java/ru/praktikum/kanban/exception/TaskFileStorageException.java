package ru.praktikum.kanban.exception;

public class TaskFileStorageException extends Exception {
    public TaskFileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
