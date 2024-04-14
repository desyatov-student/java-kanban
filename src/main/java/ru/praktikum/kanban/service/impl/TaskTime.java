package ru.praktikum.kanban.service.impl;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import ru.praktikum.kanban.dto.UpdateTaskDto;
import ru.praktikum.kanban.model.Task;

@Getter
@ToString
@AllArgsConstructor
public class TaskTime {
    @NonNull private LocalDateTime startTime;
    @NonNull private LocalDateTime endTime;

    public static TaskTime of(Task task) {
        return new TaskTime(task.getStartTime(), task.getEndTime());
    }

    public static TaskTime of(UpdateTaskDto updateTaskDto) {
        return new TaskTime(updateTaskDto.getStartTime(), updateTaskDto.getEndTime());
    }
}
