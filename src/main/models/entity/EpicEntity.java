package main.models.entity;

import main.models.BaseTask;
import main.models.TaskStatus;

public class EpicEntity extends BaseTask {

    private TaskStatus status;
    public TaskStatus getStatus() {
        return status;
    }

    public EpicEntity(int id, String name, String description, TaskStatus status) {
        super(id, name, description);
        this.status = status;
    }
}
