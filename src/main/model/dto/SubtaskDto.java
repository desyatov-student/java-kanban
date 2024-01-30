package main.model.dto;

import java.util.Objects;
import main.model.BaseTask;
import main.model.TaskStatus;

public class SubtaskDto extends BaseTaskDto {

    public SubtaskDto(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
    }

}
