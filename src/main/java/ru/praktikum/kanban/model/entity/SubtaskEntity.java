package ru.praktikum.kanban.model.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import ru.praktikum.kanban.model.TaskStatus;

@Setter
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SubtaskEntity extends BaseTaskEntity {

    @EqualsAndHashCode.Exclude
    private int epicId;

    public SubtaskEntity(
            int id,
            @NonNull String name,
            @NonNull String description,
            @NonNull TaskStatus status,
            int epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }
}
