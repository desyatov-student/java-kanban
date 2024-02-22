package ru.praktikum.kanban.model.dto.update;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public abstract class BaseUpdateTask {
    private final int id;
    private final String name;
    private final String description;

    public BaseUpdateTask(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
