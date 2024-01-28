package main.models.entity;

import java.util.List;
import main.models.BaseTask;
import main.models.TaskStatus;

public class EpicEntity extends BaseTask {

    private List<Integer> subtasksIds;

    private TaskStatus status;
    public TaskStatus getStatus() {
        return status;
    }

    public EpicEntity(int id, String name, String description, TaskStatus status, List<Integer> subtasksIds) {
        super(id, name, description);
        this.subtasksIds = subtasksIds;
        this.status = status;
    }
}
