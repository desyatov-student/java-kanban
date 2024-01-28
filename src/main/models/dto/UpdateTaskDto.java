package main.models.dto;

import main.models.TaskStatus;
import main.models.BaseTask;

public class UpdateTaskDto extends BaseTask {

    private TaskStatus status;

    public TaskStatus getStatus() {
        return status;
    }

    public UpdateTaskDto(int id, String name, String description, TaskStatus status) {
        super(id, name, description);
        this.status = status;
    }
}
