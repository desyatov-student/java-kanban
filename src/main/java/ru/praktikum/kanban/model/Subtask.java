package ru.praktikum.kanban.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Subtask extends Task {

    @EqualsAndHashCode.Exclude
    private int epicId;

    public Subtask(
            int id,
            @NonNull String name,
            @NonNull String description,
            @NonNull TaskStatus status,
            int epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }
}
