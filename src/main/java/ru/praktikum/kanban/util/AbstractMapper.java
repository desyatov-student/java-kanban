package ru.praktikum.kanban.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class AbstractMapper<T, R> {
    Map<Object, Function<T, R>> actions;

    public AbstractMapper(Map<Object, Function<T, R>> actions) {
        this.actions = actions;
    }

    public AbstractMapper() {
        this.actions = new HashMap<>();
    }

    public void put(Object key, Function<T, R> value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Argument is null");
        }
        actions.put(key, value);
    }

    public R tryMap(T object) {
        if (object == null) {
            throw new IllegalArgumentException("Argument is null");
        }
        Object key = object.getClass();
        if (!actions.containsKey(key)) {
            throw new IllegalArgumentException("No action for key=" + key + " keys: " + actions.keySet());
        }
        Function<T, R> action = actions.get(key);
        return action.apply(object);
    }
}

