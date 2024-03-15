package ru.praktikum.kanban.model.mapper;

import java.util.HashMap;
import ru.praktikum.kanban.model.TaskType;

public interface BaseTaskMapper<T> {

    class Input<T> {
        String[] values;
        TaskType taskType;
        HashMap<Integer, T> models;

        public Input(String[] values, TaskType taskType, HashMap<Integer, T> models) {
            this.values = values;
            this.taskType = taskType;
            this.models = models;
        }
    }

    String toString(T task);

    T toModel(Input<T> input);
}
