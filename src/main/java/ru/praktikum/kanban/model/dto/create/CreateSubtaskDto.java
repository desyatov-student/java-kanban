package ru.praktikum.kanban.model.dto.create;

import lombok.Getter;

@Getter
public class CreateSubtaskDto extends BaseCreateTask {

    private final int epicId;

    public CreateSubtaskDto(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }
}
