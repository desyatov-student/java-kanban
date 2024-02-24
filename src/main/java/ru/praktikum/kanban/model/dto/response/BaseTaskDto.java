package ru.praktikum.kanban.model.dto.response;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import ru.praktikum.kanban.model.TaskStatus;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class BaseTaskDto {
    private final int id;
    @NonNull private final String name;
    @NonNull private final String description;
    @NonNull private final TaskStatus status;
}
