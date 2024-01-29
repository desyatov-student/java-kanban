package main.models.dto;

import main.models.BaseTask;

public class CreateEpicDto extends BaseTask {

    public CreateEpicDto(int id, String name, String description) {
        super(id, name, description);
    }
}
