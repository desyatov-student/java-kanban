package ru.praktikum.kanban.model;

import java.time.Duration;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.Getter;

@Setter
@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = {"id"})
public class Task {
    @Setter(AccessLevel.NONE)
    @NonNull private final Integer id;
    @NonNull private String name;
    @NonNull private String description;
    @NonNull private TaskStatus status;
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
