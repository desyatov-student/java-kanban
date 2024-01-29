package main.models.dto;

import main.models.BaseTask;
import main.models.TaskStatus;

public class CreateSubtaskDto extends BaseTask {

    private TaskStatus status;

    public TaskStatus getStatus() {
        return status;
    }

    public CreateSubtaskDto(int id, String name, String description) {
        super(id, name, description);
        this.status = TaskStatus.NEW;
    }
}
