package main.models.dto;

import main.models.TaskStatus;
import main.models.BaseTask;

public class UpdateSubtaskDto extends BaseTask {

    private TaskStatus status;

    public TaskStatus getStatus() {
        return status;
    }

    public UpdateSubtaskDto(int id, String name, String description, TaskStatus status) {
        super(id, name, description);
        this.status = status;
    }
}
