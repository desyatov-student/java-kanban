package ru.praktikum.kanban.model.dto.create;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.praktikum.kanban.model.TaskStatus;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public abstract class BaseCreateTask {
    private final String name;
    private final String description;
    private final TaskStatus status = TaskStatus.NEW;
}
