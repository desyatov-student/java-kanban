package main.model.dto.create;

import main.model.BaseTask;
import main.model.TaskStatus;

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
