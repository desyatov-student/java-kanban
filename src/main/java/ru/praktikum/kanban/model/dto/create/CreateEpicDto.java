package ru.praktikum.kanban.model.dto.create;

import lombok.ToString;

@ToString(callSuper = true)
public class CreateEpicDto extends BaseCreateTask {

    public CreateEpicDto(String name, String description) {
        super(name, description);
    }
}
