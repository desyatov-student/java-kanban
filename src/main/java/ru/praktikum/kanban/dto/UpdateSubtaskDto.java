package ru.praktikum.kanban.dto;

import java.time.Duration;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import ru.praktikum.kanban.model.TaskStatus;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UpdateSubtaskDto extends UpdateTaskDto {

    public UpdateSubtaskDto(
            @NonNull String name,
            @NonNull String description,
            @NonNull TaskStatus status,
            LocalDateTime startTime,
            Duration duration
    ) {
        super(name, description, status, startTime, duration);
    }

}
