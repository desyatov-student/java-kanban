package ru.praktikum.kanban.model.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.praktikum.kanban.model.TaskStatus;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = {"id"})
public abstract class BaseTaskEntity {
    private final int id;
    @Setter
    public String name;
    @Setter
    public String description;
    @Setter
    public TaskStatus status;
}
