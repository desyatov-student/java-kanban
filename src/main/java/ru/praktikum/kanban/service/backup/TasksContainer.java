package ru.praktikum.kanban.service.backup;

import java.util.HashMap;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.praktikum.kanban.model.entity.Epic;
import ru.praktikum.kanban.model.entity.Subtask;
import ru.praktikum.kanban.model.entity.Task;

@EqualsAndHashCode
@ToString
public class TasksContainer {
    public final HashMap<Integer, Epic> epics;
    public final HashMap<Integer, Subtask> subtasks;
    public final HashMap<Integer, Task> tasks;

    public TasksContainer() {
        this(new HashMap<>(), new HashMap<>(), new HashMap<>());
    }

    public TasksContainer(
            HashMap<Integer, Epic> epics,
            HashMap<Integer, Subtask> subtasks,
            HashMap<Integer, Task> tasks
    ) {
        this.epics = new HashMap<>(epics);
        this.subtasks = new HashMap<>(subtasks);
        this.tasks = new HashMap<>(tasks);
    }

    public void addEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void addSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
    }

    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }
}
