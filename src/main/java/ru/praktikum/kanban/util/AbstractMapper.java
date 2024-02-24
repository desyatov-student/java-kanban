package ru.praktikum.kanban.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class AbstractMapper<T, R> {
    Map<Class, Function<T, R>> actions;

    public AbstractMapper(Map<Class, Function<T, R>> actions) {
        this.actions = actions;
    }

    public AbstractMapper() {
        this.actions = new HashMap<>();
    }

    public void put(Class key, Function<T, R> value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Argument is null");
        }
        actions.put(key, value);
    }

    public R tryMap(T object) {
        if (object == null) {
            throw new IllegalArgumentException("Argument is null");
        }
        Class key = object.getClass();
        if (!actions.containsKey(key)) {
            throw new IllegalArgumentException("No action for key=" + key);
        }
        Function<T, R> action = actions.get(key);
        return action.apply(object);
    }
}

