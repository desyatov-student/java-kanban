package ru.praktikum.kanban.model.entity;

import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import ru.praktikum.kanban.model.TaskStatus;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class EpicEntity extends BaseTaskEntity {

    @EqualsAndHashCode.Exclude
    public final List<Integer> subtasks;

    public EpicEntity(
            int id,
            @NonNull String name,
            @NonNull String description,
            @NonNull TaskStatus status,
            @NonNull List<Integer> subtasks) {
        super(id, name, description, status);
        this.subtasks = new ArrayList<>(subtasks);
    }
}
