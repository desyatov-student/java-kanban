package ru.praktikum.kanban.service.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import ru.praktikum.kanban.model.Task;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.praktikum.kanban.helper.TaskFactory.TASK;

public class TaskValidatorTest {

    TaskValidator validator = new TaskValidator();

    @Test
    void hasIntersectionOfTimeShouldReturnFalseWhenTaskDoesNotIntersectCollectionsOfTasks() {
        // given
        // 8:00-9:00
        Task task1 = TASK(1, time(8, 0), duration());
        List<Task> tasks = List.of(
                // 8:00-9:00 6:00-7:00
                TASK(2, time(6, 0), duration()),
                // 8:00-9:00 7:00-8:00
                TASK(2, time(7, 0), duration()),
                // 8:00-9:00 9:00-10:00
                TASK(2, time(9, 0), duration()),
                // 8:00-9:00 10:00-11:00
                TASK(2, time(10, 0), duration())
        );

        // when
        boolean result = validator.hasIntersectionOfTime(task1, tasks);

        // then
        assertFalse(result);
    }

    @Test
    void hasIntersectionOfTimeShouldReturnTrueWhenTaskIntersectsCollectionsOfTasks() {
        // given
        // 9:30-10:30
        Task task1 = TASK(1, time(9, 30), duration());
        List<Task> tasks = List.of(
                // 9:30-10:30 10:00-11:00
                TASK(2, time(10, 0), duration()),
                // 9:30-10:30 9:00-10:00
                TASK(2, time(9, 0), duration())
        );

        // when
        boolean result = validator.hasIntersectionOfTime(task1, tasks);

        // then
        assertTrue(result);
    }

    private static LocalDateTime time(int hour, int minute) {
        return LocalDateTime.of(2024, 1, 1, hour, minute);
    }
    private static Duration duration() {
        return Duration.ofHours(1);
    }
}
