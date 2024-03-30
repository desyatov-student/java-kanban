package ru.praktikum.kanban.helper;

import java.time.Duration;
import java.time.LocalDateTime;
import lombok.Getter;

public class StartTimeGenerator {
    private LocalDateTime startTime;
    @Getter
    private final Duration duration;
    @Getter
    private LocalDateTime endTime;

    public LocalDateTime getStartTime() {
        LocalDateTime oldStartTime = startTime;
        startTime = oldStartTime.plusDays(1);
        endTime = startTime.plus(duration);
        return oldStartTime;
    }

    public StartTimeGenerator() {
        this.startTime = LocalDateTime.now();
        this.duration = Duration.ofMinutes(30);
        this.endTime = startTime.plus(duration);
    }
}
