package main.repository;

import java.util.List;
import main.model.entity.TaskEntity;

public interface TaskRepository {

    List<TaskEntity> getAllTasks();
    void saveTask(TaskEntity taskEntity);
    TaskEntity getTask(int taskId);
    void removeTask(int taskId);
    void removeAllTasks();
}

