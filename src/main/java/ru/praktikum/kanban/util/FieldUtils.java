package ru.praktikum.kanban.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Stream;

public final class FieldUtils {
    private FieldUtils() {
    }

    public static Stream<Field> getFields(Class<?> cls) {
        Field[] superclassFields = {};
        if (!cls.equals(Object.class)) {
            superclassFields = cls.getSuperclass().getDeclaredFields();
        }
        return Stream.of(cls.getDeclaredFields(), superclassFields)
                .flatMap(Arrays::stream);
    }

    public static Object getObjectValue(Field field, Object object) {
        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {
            return null;
        }
    }
}
