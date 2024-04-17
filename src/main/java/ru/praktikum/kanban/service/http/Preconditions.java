package ru.praktikum.kanban.service.http;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import ru.praktikum.kanban.dto.CreateTaskDto;
import ru.praktikum.kanban.dto.UpdateTaskDto;
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

    public static void checkState(UpdateTaskDto updateTaskDto) throws PreconditionsException {
        boolean hasValue = Arrays.stream(UpdateTaskDto.class.getDeclaredFields())
                .map(field -> getObjectValue(field, updateTaskDto))
                .anyMatch(Objects::nonNull);
        if (hasValue) {
            return;
        }
        throw new PreconditionsException("UpdateTaskDto is empty");
    }

    private static Object getObjectValue(Field field, Object object) {
        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (IllegalAccessException e) {
            return null;
        }
    }
}
