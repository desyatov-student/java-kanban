package ru.praktikum.kanban.repository;

import java.util.List;
import ru.praktikum.kanban.model.entity.TaskEntity;

public interface TaskRepository {

    List<TaskEntity> getAllTasks();
    void saveTask(TaskEntity simpleTaskEntity);
    TaskEntity getTask(int taskId);
    void removeTask(int taskId);
    void removeAllTasks();
}

