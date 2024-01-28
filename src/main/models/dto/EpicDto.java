package main.models.dto;

import java.util.List;
import main.models.BaseTask;
import main.models.TaskStatus;

public class EpicDto extends BaseTask {

    private List<SubtaskDto> subtasks;
    private TaskStatus status;

    public List<SubtaskDto> getSubtasks() {
        return subtasks;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public EpicDto(int id, String name, String description, TaskStatus status, List<SubtaskDto> subtasks) {
        super(id, name, description);
        this.subtasks = subtasks;
        this.status = status;
    }
}
