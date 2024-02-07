package ru.praktikum.kanban.repository;

import java.util.List;
import ru.praktikum.kanban.model.entity.SimpleTaskEntity;

public interface TaskRepository {

    List<SimpleTaskEntity> getAllTasks();
    void saveTask(SimpleTaskEntity simpleTaskEntity);
    SimpleTaskEntity getTask(int taskId);
    void removeTask(int taskId);
    void removeAllTasks();
}

