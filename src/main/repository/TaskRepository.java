package main.repository;

import java.util.List;
import main.models.entity.TaskEntity;

public interface TaskRepository {

    List<TaskEntity> getAllTasks();
    void saveTask(TaskEntity taskEntity);

}

