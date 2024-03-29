package ru.praktikum.kanban.service.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.praktikum.kanban.model.Task;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.praktikum.kanban.helper.TaskFactory.TASK;

public class TaskScheduleValidatorTest {

    TaskScheduleValidator validator;

    @BeforeEach
    void setUp() {
        LocalDateTime startTime = time(0, 0);
        Duration duration = Duration.ofHours(4);
        Duration segmentDuration = Duration.ofMinutes(15);
        validator = new TaskScheduleValidator(duration, segmentDuration, startTime);
    }

    @Test
    void checkIntersectionAndUpdateScheduleShouldReturnFalseWhenTaskDoesNotHasIntersectionByTime() {
        // given
        // 8:00-9:00
        Task task1 = TASK(1, time(8, 0), duration());
        List<Task> tasks = List.of(
                // 6:00-7:00
                TASK(2, time(6, 0), duration()),
                // 7:00-8:00
                TASK(2, time(7, 0), duration()),
                // 9:00-10:00
                TASK(2, time(9, 0), duration()),
                // 10:00-11:00
                TASK(2, time(10, 0), duration())
        );
        validator.resetSchedule(tasks);

        // when
        boolean result1 = validator.checkIntersectionAndUpdateSchedule(task1);

        // then
        assertFalse(result1);
    }

    @Test
    void checkIntersectionAndUpdateScheduleShouldReturnTrueWhenTaskHasIntersectionByTime() {
        // given
        // 8:30-9:30
        Task task1 = TASK(1, time(8, 30), duration());
        List<Task> tasks = List.of(
                // 6:00-7:00
                TASK(2, time(6, 0), duration()),
                // 7:00-8:00
                TASK(2, time(7, 0), duration()),
                // 9:00-10:00
                TASK(2, time(9, 0), duration()),
                // 10:00-11:00
                TASK(2, time(10, 0), duration())
        );
        validator.resetSchedule(tasks);

        // when
        boolean result1 = validator.checkIntersectionAndUpdateSchedule(task1);

        // then
        assertTrue(result1);
    }

    @Test
    void checkIntersectionAndUpdateScheduleShouldUpdateScheduleAndReturnTrueWhenCallMethodTwice() {
        // given
        // 8:00-9:00
        Task task1 = TASK(1, time(8, 0), duration());

        // when
        boolean result1 = validator.checkIntersectionAndUpdateSchedule(task1);
        boolean result2 = validator.checkIntersectionAndUpdateSchedule(task1);

        // then
        assertFalse(result1);
        assertTrue(result2);
    }

    private static LocalDateTime time(int hour, int minute) {
        return LocalDateTime.of(2000, 1, 1, hour, minute);
    }
    private static Duration duration() {
        return Duration.ofHours(1);
    }
}
