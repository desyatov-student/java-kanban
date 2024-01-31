package main.model.dto.create;

import main.model.BaseTask;

public class CreateEpicDto extends BaseCreateTaskDto {

    public CreateEpicDto(String name, String description) {
        super(name, description);
    }
}
