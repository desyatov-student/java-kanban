package ru.praktikum.kanban.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CreateSubtaskDto extends CreateTaskDto {

    private final Integer epicId;

    public CreateSubtaskDto(String name, String description, Integer epicId) {
        super(name, description);
        this.epicId = epicId;
    }
}
