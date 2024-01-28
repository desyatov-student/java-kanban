package main.models.dto;

import main.models.BaseTask;
import main.models.TaskStatus;

public class SubtaskDto extends BaseTask {

    private EpicDto epic;
    private TaskStatus status;
    public TaskStatus getStatus() {
        return status;
    }

    public SubtaskDto(int id, String name, String description, TaskStatus status, EpicDto epic) {
        super(id, name, description);
        this.epic = epic;
        this.status = status;
    }
}
