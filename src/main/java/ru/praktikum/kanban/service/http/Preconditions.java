package ru.praktikum.kanban.service.http;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import ru.praktikum.kanban.exception.PreconditionsException;

public final class Preconditions {
    private Preconditions() {
    }

    public static <T> void checkRequiredValues(T object) throws PreconditionsException {
        List<String> required = List.of("name", "description");
        List<Field> fields = Arrays.stream(object.getClass().getDeclaredFields())
                .filter(field -> required.contains(field.getName()))
                .collect(Collectors.toList());
        for (Field field : fields) {
            Object value = getObjectValue(field, object);
            checkNotNull(value, String.format("Property %s is null", field.getName()));
        }
    }

    public static <T> void checkEmpty(T object) throws PreconditionsException {
        boolean hasValue = Arrays.stream(object.getClass().getDeclaredFields())
                .map(field -> getObjectValue(field, object))
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

    public static Integer getIntValue(String key, Map<String, String> params) throws PreconditionsException {
        String value = params.get(key);
        try {
            return Integer.parseInt(value);
        } catch (Exception exception) {
            throw new PreconditionsException("Could not parse value: " + value);
        }
    }
}
