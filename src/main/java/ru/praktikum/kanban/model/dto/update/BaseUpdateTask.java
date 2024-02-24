package ru.praktikum.kanban.model.dto.update;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public abstract class BaseUpdateTask {
    private final int id;
    @NonNull private final String name;
    @NonNull private final String description;

    public BaseUpdateTask(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
