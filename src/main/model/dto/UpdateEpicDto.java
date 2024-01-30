package main.model.dto;

import main.model.BaseTask;

public class UpdateEpicDto extends BaseTask {

    public UpdateEpicDto(int id, String name, String description) {
        super(id, name, description);
    }
}
