package main.model.dto.create;

import main.model.BaseTask;
import main.model.TaskStatus;

public class CreateTaskDto extends BaseCreateTaskDto {

    public CreateTaskDto(String name, String description) {
        super(name, description);
    }
}
