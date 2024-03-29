package ru.praktikum.kanban.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import ru.praktikum.kanban.service.backup.TasksContainer;
import ru.praktikum.kanban.model.Epic;
import ru.praktikum.kanban.model.Subtask;
import ru.praktikum.kanban.model.Task;

public final class CollectionsHelper {
    private CollectionsHelper() {
    }

    public static TasksContainer tasksListsToContainer(
            List<Epic> epics,
            List<Subtask> subtasks,
            List<Task> tasks
    ) {
        Map<Integer, Epic> epicMap = epics.stream()
                .collect(Collectors.toMap(Epic::getId, Function.identity()));
        Map<Integer, Subtask> subtasksMap = subtasks.stream()
                .collect(Collectors.toMap(Subtask::getId, Function.identity()));
        Map<Integer, Task> tasksMap = tasks.stream()
                .collect(Collectors.toMap(Task::getId, Function.identity()));

        return new TasksContainer(new HashMap<>(epicMap), new HashMap<>(subtasksMap), new HashMap<>(tasksMap));
    }
}
