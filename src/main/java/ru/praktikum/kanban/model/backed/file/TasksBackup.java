package ru.praktikum.kanban.model.backed.file;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.Getter;
import ru.praktikum.kanban.model.TaskType;
import ru.praktikum.kanban.model.entity.BaseTaskEntity;
import ru.praktikum.kanban.model.mapper.CollectionsHelper;

@Getter
public class TasksBackup {
    HashMap<TaskType, HashMap<Integer, BaseTaskEntity>> tasks;
    List<BaseTaskEntity> history;

    public TasksBackup() {
        this.tasks = new HashMap<>();
        this.history = new ArrayList<>();
    }

    public TasksBackup(HashMap<TaskType, HashMap<Integer, BaseTaskEntity>> tasks, List<BaseTaskEntity> history) {
        this.tasks = tasks;
        this.history = history;
    }

    public List<BaseTaskEntity> getTasksList() {
        return CollectionsHelper.mapToList(getTasks());
    }
}
