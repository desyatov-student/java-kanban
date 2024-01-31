package main.model.dto.response;

import main.model.TaskStatus;

public class TaskDto extends BaseTaskDto {

    public TaskDto(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
    }
}
