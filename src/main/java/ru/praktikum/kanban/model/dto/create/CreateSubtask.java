package ru.praktikum.kanban.model.dto.create;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CreateSubtask extends CreateTask {

    private final int epicId;

    public CreateSubtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }
}
