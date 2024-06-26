package ru.praktikum.kanban.dto;

import java.time.Duration;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import ru.praktikum.kanban.model.TaskStatus;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SubtaskDto extends TaskDto {

    public SubtaskDto(
            @NonNull Integer id,
            @NonNull String name,
            @NonNull String description,
            @NonNull TaskStatus status,
            LocalDateTime startTime,
            Duration duration,
            LocalDateTime endTime
    ) {
        super(id, name, description, status, startTime, duration, endTime);
    }

}
