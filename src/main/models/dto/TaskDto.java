package main.models.dto;

import main.models.BaseTask;
import main.models.TaskStatus;

public class TaskDto extends BaseTask {

    private TaskStatus status;
    public TaskStatus getStatus() {
        return status;
    }

    public TaskDto(int id, String name, String description, TaskStatus status) {
        super(id, name, description);
        this.status = status;
    }
}
