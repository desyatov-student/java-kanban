package ru.praktikum.kanban.model.dto.create;

import lombok.ToString;

@ToString(callSuper = true)
public class CreateTaskDto extends BaseCreateTask {

    public CreateTaskDto(String name, String description) {
        super(name, description);
    }
}
