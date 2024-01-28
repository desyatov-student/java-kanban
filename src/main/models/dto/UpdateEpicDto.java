package main.models.dto;

import main.models.BaseTask;

public class UpdateEpicDto extends BaseTask {

    public UpdateEpicDto(int id, String name, String description) {
        super(id, name, description);
    }
}
