package ru.praktikum.kanban.model.entity;

import lombok.Getter;
import lombok.Setter;
import ru.praktikum.kanban.model.TaskStatus;

@Setter
@Getter
public class SubtaskEntity extends BaseTaskEntity {

    private int epicId;

    public SubtaskEntity(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
    }
}
