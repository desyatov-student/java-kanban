package ru.praktikum.kanban.service.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import ru.praktikum.kanban.model.Task;

public class TaskScheduleValidator {

    private static final int SEGMENT_DURATION_IN_MINUTES = 15;
    private final HashMap<LocalDateTime, Boolean> schedule;
    private final Duration segment;

    public TaskScheduleValidator() {
        this(Duration.ofMinutes(SEGMENT_DURATION_IN_MINUTES));
    }

    public TaskScheduleValidator(Duration segment) {
        this.schedule = new HashMap<>();
        this.segment = segment;
        initializeScheduleMap();
    }

    // метод используется когда восстанавливаем таски из файла
    public void resetSchedule(List<Task> tasks) {
        initializeScheduleMap();
        if (tasks.isEmpty()) {
            return;
        }
        List<Task> sortedTasks = tasks.stream()
                .sorted(Comparator.comparing(Task::getStartTime))
                .collect(Collectors.toList());

        // для каждой таски находим все временные интервалы и помечаем занятость в таблице расписания
        for (Task task : sortedTasks) {
            splitTimeToSegments(task.getStartTime(), task.getEndTime(), segment, (segment) -> {
                schedule.put(segment, true);
                return true;
            });
        }
    }

    // При обновлении расписания таски старое нужно убрать чтобы не было ложного пересечения.
    public void resetScheduleForTaskTime(TaskTime taskTime) {
        splitTimeToSegments(taskTime.getStartTime(), taskTime.getEndTime(), segment, (segment) -> {
            schedule.put(segment, false);
            return true;
        });
    }

    public boolean checkIntersectionAndUpdateSchedule(TaskTime taskTime) {
        HashMap<LocalDateTime, Boolean> scheduleUpdate = new HashMap<>();
        AtomicBoolean hasIntersection = new AtomicBoolean(false);
        splitTimeToSegments(taskTime.getStartTime(), taskTime.getEndTime(), segment, (segment) -> {
            if (schedule.getOrDefault(segment, false)) {
                // Останавливаем перебор сегментов т.к. получили пересечений.
                hasIntersection.set(true);
                return false;
            } else {
                // если нет пересечения продолжаем цикл и добавляем сегменты во временную переменную
                scheduleUpdate.put(segment, true);
                return true;
            }
        });

        if (hasIntersection.get()) {
            return true;
        } else {
            // если не было пересечения добавляем в общее расписание новые интервалы
            schedule.putAll(scheduleUpdate);
            return false;
        }
    }

    private void initializeScheduleMap() {
        schedule.clear();
    }

    // Разделяет временной интервал на кусочки длинной segmentDuration. Можно остановить разделение с помощью predicate.
    private void splitTimeToSegments(
            LocalDateTime rawStartTime,
            LocalDateTime rawEndTime,
            Duration segmentDuration,
            Predicate<LocalDateTime> predicate
    ) {

        LocalDateTime startTime = roundToLow(rawStartTime);
        LocalDateTime endTime = roundToHigh(rawEndTime);
        LocalDateTime currentTime = startTime;
        boolean continueIterating;
        do {
            continueIterating = predicate.test(currentTime);
            currentTime = currentTime.plus(segmentDuration);
        } while (currentTime.isBefore(endTime) && continueIterating);
    }

    // Округляет в нижнюю границу. 00:12 -> 00:00, 00:18 -> 00:15
    private LocalDateTime roundToLow(LocalDateTime time) {
        return time.truncatedTo(ChronoUnit.HOURS)
                .plusMinutes(segment.toMinutes() * (time.getMinute() / segment.toMinutes()));
    }

    // Округляет в верхнюю границу. 00:12 -> 00:15, 00:50 -> 01:00
    private LocalDateTime roundToHigh(LocalDateTime time) {
        return time.truncatedTo(ChronoUnit.HOURS)
                .plusMinutes(segment.toMinutes() * (int) Math.ceil((double) time.getMinute() / segment.toMinutes()));
    }
}
