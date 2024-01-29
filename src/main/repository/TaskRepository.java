package main.repository;

import java.util.List;
import main.models.entity.TaskEntity;

public interface TaskRepository {

    public List<TaskEntity> getAllTasks();
    public void saveTask(TaskEntity taskEntity);

}

