package main.model.dto.create;

import main.model.BaseTask;
import main.model.TaskStatus;

public class CreateTaskDto extends BaseTask {

    private TaskStatus status;

    public TaskStatus getStatus() {
        return status;
    }

    public CreateTaskDto(int id, String name, String description) {
        super(id, name, description);
        this.status = TaskStatus.NEW;
    }
}
