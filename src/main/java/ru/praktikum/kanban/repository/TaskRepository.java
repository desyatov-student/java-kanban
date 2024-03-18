package ru.praktikum.kanban.repository;

import java.util.List;
import ru.praktikum.kanban.model.Task;

public interface TaskRepository {

    List<Task> getAllTasks();

    void saveTask(Task simpleTaskEntity);

    Task getTask(int taskId);

    void removeTask(int taskId);

    void removeAllTasks();
}

