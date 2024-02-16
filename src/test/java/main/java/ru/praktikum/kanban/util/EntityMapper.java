package main.java.ru.praktikum.kanban.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class EntityMapper<T, R> {
    Map<Class, Function<T, R>> actions;

    public EntityMapper(Map<Class, Function<T, R>> actions) {
        this.actions = actions;
    }

    public EntityMapper() {
        this.actions = new HashMap<>();
    }

    public void put(Class key, Function<T, R> value) {
        actions.put(key, value);
    }

    public R mapToTaskDto(T taskEntity) {
        if (taskEntity == null) {
            throw new IllegalArgumentException("Argument is null");
        }
        Class key = taskEntity.getClass();
        if (!actions.containsKey(key)) {
            throw new IllegalArgumentException("No action for key=" + key);
        }
        Function<T, R> action = actions.get(key);
        return action.apply(taskEntity);
    }
}

