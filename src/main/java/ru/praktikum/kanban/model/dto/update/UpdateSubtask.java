package ru.praktikum.kanban.model.dto.update;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import ru.praktikum.kanban.model.TaskStatus;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class UpdateSubtask {
    private final int id;
    @NonNull private final String name;
    @NonNull private final String description;
    @NonNull private final TaskStatus status;
}
