package main.model.dto;

import main.model.TaskStatus;
import main.model.BaseTask;

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
