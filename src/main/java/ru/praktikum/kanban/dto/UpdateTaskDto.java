package ru.praktikum.kanban.dto;

import java.time.Duration;
import java.time.LocalDateTime;
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
public class UpdateTaskDto {
    @NonNull private final String name;
    @NonNull private final String description;
    @NonNull private final TaskStatus status;
    private LocalDateTime startTime;
    private Duration duration;

    public LocalDateTime getEndTime() {
        if (isTimeEmpty()) {
            return null;
        }
        return startTime.plus(duration);
    }

    public boolean isTimeEmpty() {
        return startTime == null || duration == null;
    }
}
