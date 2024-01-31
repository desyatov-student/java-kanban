package main.model.dto.response;

import main.model.TaskStatus;

public class SubtaskDto extends BaseTaskDto {

    public SubtaskDto(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
    }

}
