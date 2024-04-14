package ru.praktikum.kanban.dto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import ru.praktikum.kanban.model.TaskStatus;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class EpicDto extends TaskDto {

    @NonNull private final List<SubtaskDto> subtasks;

    public EpicDto(
            @NonNull Integer id,
            @NonNull String name,
            @NonNull String description,
            @NonNull TaskStatus status,
            @NonNull List<SubtaskDto> subtasks,
            LocalDateTime startTime,
            Duration duration,
            LocalDateTime endTime
    ) {
        super(id, name, description, status, startTime, duration, endTime);
        this.subtasks = subtasks;
    }
}
