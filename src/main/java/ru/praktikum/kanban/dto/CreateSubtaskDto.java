package ru.praktikum.kanban.dto;

import java.time.Duration;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CreateSubtaskDto extends CreateTaskDto {

    private final Integer epicId;

    public CreateSubtaskDto(
            @NonNull String name,
            @NonNull String description,
            @NonNull Integer epicId,
            LocalDateTime startTime,
            Duration duration
    ) {
        super(name, description, startTime, duration);
        this.epicId = epicId;
    }
}
