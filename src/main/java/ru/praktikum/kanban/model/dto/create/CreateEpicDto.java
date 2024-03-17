package ru.praktikum.kanban.model.dto.create;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CreateEpicDto extends CreateTaskDto {

    public CreateEpicDto(String name, String description) {
        super(name, description);
    }
}
