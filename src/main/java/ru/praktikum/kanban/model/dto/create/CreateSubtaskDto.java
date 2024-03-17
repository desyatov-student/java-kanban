package ru.praktikum.kanban.model.dto.create;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CreateSubtaskDto extends CreateTaskDto {

    private final int epicId;

    public CreateSubtaskDto(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }
}
