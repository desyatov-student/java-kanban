package ru.praktikum.kanban.service.http;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import ru.praktikum.kanban.dto.CreateTaskDto;
import ru.praktikum.kanban.dto.UpdateTaskDto;
import ru.praktikum.kanban.exception.PreconditionsException;

public final class Preconditions {
    private Preconditions() {
    }

    public static void checkState(CreateTaskDto createTaskDto) throws PreconditionsException {
        checkNotNull(createTaskDto.getName(), "Property name is null");
        checkNotNull(createTaskDto.getDescription(), "Property description is null");
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

    public static <T> void checkNotNull(T value, String message) throws PreconditionsException {
        if (value == null) {
            throw new PreconditionsException(message);
        }
    }
}
