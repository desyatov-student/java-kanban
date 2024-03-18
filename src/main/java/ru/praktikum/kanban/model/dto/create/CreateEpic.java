package ru.praktikum.kanban.model.dto.create;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CreateEpic extends CreateTask {

    public CreateEpic(String name, String description) {
        super(name, description);
    }
}
