package main.model.dto.create;

import main.model.BaseTask;

public class CreateEpicDto extends BaseTask {

    public CreateEpicDto(int id, String name, String description) {
        super(id, name, description);
    }
}
