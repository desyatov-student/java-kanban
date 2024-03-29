package ru.praktikum.kanban.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import ru.praktikum.kanban.model.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class UpdateSubtaskDto {
    @NonNull private final Integer id;
    @NonNull private final String name;
    @NonNull private final String description;
    @NonNull private final TaskStatus status;
    private final LocalDateTime startTime;
    private final Duration duration;
}
