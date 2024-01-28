package main.models.entity;

import main.models.BaseTask;
import main.models.TaskStatus;

public class SubtaskEntity extends BaseTask {

    private int epicId;
    private TaskStatus status;
    public TaskStatus getStatus() {
        return status;
    }

    public SubtaskEntity(int id, String name, String description, TaskStatus status, int epicId) {
        super(id, name, description);
        this.epicId = epicId;
        this.status = status;
    }
}
