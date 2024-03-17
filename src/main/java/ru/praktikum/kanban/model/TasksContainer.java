package ru.praktikum.kanban.model;

import java.util.HashMap;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.praktikum.kanban.model.entity.EpicEntity;
import ru.praktikum.kanban.model.entity.SubtaskEntity;
import ru.praktikum.kanban.model.entity.TaskEntity;

@EqualsAndHashCode
@ToString
public class TasksContainer {
    public final HashMap<Integer, EpicEntity> epics;
    public final HashMap<Integer, SubtaskEntity> subtasks;
    public final HashMap<Integer, TaskEntity> tasks;

    public TasksContainer() {
        this(new HashMap<>(), new HashMap<>(), new HashMap<>());
    }

    public TasksContainer(
            HashMap<Integer, EpicEntity> epics,
            HashMap<Integer, SubtaskEntity> subtasks,
            HashMap<Integer, TaskEntity> tasks
    ) {
        this.epics = new HashMap<>(epics);
        this.subtasks = new HashMap<>(subtasks);
        this.tasks = new HashMap<>(tasks);
    }

    public void addEpic(EpicEntity epic) {
        epics.put(epic.getId(), epic);
    }

    public void addSubtask(SubtaskEntity subtask) {
        subtasks.put(subtask.getId(), subtask);
    }

    public void addTask(TaskEntity task) {
        tasks.put(task.getId(), task);
    }
}
