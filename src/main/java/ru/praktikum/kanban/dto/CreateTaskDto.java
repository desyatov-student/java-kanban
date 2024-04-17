package ru.praktikum.kanban.dto;

import java.time.Duration;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class CreateTaskDto {
    @NonNull private final String name;
    @NonNull private final String description;
    private final LocalDateTime startTime;
    private final Duration duration;
}
