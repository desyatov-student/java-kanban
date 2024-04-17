package ru.praktikum.kanban.service.http;

import ru.praktikum.kanban.dto.CreateTaskDto;
import ru.praktikum.kanban.exception.PreconditionsException;

import static lombok.Lombok.checkNotNull;

public final class Preconditions {
    private Preconditions() {
    }

    public static void checkState(CreateTaskDto createTaskDto) throws PreconditionsException {
        try {
            checkNotNull(createTaskDto.getName(), "Property name is null");
            checkNotNull(createTaskDto.getDescription(), "Property description is null");
        } catch (NullPointerException e) {
            throw new PreconditionsException(e.getMessage());
        }
    }
}
