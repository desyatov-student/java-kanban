package ru.praktikum.kanban.model;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static ru.praktikum.kanban.helper.TaskFactory.EPIC;
import static ru.praktikum.kanban.helper.TaskFactory.SUBTASK;
import static ru.praktikum.kanban.helper.TaskFactory.TASK;

public class TaskTest {

    @ParameterizedTest
    @MethodSource("provideModels")
    void testEquals(Task task1, Task task2, boolean isEquals) {
        if (isEquals) {
            assertEquals(task1, task2);
        } else {
            assertNotEquals(task1, task2);
        }
    }

    private static Stream<Arguments> provideModels() {
        return Stream.of(
                Arguments.of(
                        EPIC(1, TaskStatus.NEW),
                        EPIC(1, TaskStatus.DONE),
                        true
                ),
                Arguments.of(
                        EPIC(1, TaskStatus.NEW),
                        EPIC(2, TaskStatus.NEW),
                        false
                ),
                Arguments.of(
                        SUBTASK(1, TaskStatus.NEW),
                        SUBTASK(1, TaskStatus.DONE),
                        true
                ),
                Arguments.of(
                        SUBTASK(1, TaskStatus.NEW),
                        SUBTASK(2, TaskStatus.NEW),
                        false
                ),
                Arguments.of(
                        TASK(1, TaskStatus.NEW),
                        TASK(1, TaskStatus.DONE),
                        true
                ),
                Arguments.of(
                        TASK(1, TaskStatus.NEW),
                        TASK(2, TaskStatus.NEW),
                        false
                ),
                Arguments.of(
                        TASK(1, TaskStatus.NEW),
                        EPIC(1, TaskStatus.NEW),
                        false
                ),
                Arguments.of(
                        SUBTASK(1, TaskStatus.NEW),
                        EPIC(1, TaskStatus.NEW),
                        false
                ),
                Arguments.of(
                        SUBTASK(1, TaskStatus.NEW),
                        TASK(1, TaskStatus.NEW),
                        false
                )
        );
    }
}
