package main.model.dto;

import main.model.BaseTask;
import main.model.TaskStatus;

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
