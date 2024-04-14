package ru.praktikum.kanban.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Epic extends Task {

    @EqualsAndHashCode.Exclude
    public final List<Integer> subtasks;
    @EqualsAndHashCode.Exclude
    private LocalDateTime endTime;

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public Epic(
            @NonNull Integer id,
            @NonNull String name,
            @NonNull String description,
            @NonNull TaskStatus status,
            @NonNull List<Integer> subtasks,
            LocalDateTime startTime,
            Duration duration,
            LocalDateTime endTime
    ) {
        super(id, name, description, status, startTime, duration);
        this.subtasks = new ArrayList<>(subtasks);
        this.endTime = endTime;
    }
}
