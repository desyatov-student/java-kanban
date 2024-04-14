package ru.praktikum.kanban.model;

import java.time.Duration;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Subtask extends Task {

    @EqualsAndHashCode.Exclude
    private Integer epicId;

    public Subtask(
            @NonNull Integer id,
            @NonNull String name,
            @NonNull String description,
            @NonNull TaskStatus status,
            @NonNull Integer epicId,
            LocalDateTime startTime,
            Duration duration
    ) {
        super(id, name, description, status, startTime, duration);
        this.epicId = epicId;
    }
}
