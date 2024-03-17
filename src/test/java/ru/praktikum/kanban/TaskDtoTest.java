package ru.praktikum.kanban;

import java.util.ArrayList;
import java.util.List;
import ru.praktikum.kanban.model.dto.response.TaskDto;
import ru.praktikum.kanban.model.dto.response.EpicDto;
import ru.praktikum.kanban.model.dto.response.SubtaskDto;
import ru.praktikum.kanban.model.TaskStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TaskDtoTest {

    private static class TestExample {
        TaskDto task1;
        TaskDto task2;
        boolean isEquals;

        public TestExample(TaskDto task1, TaskDto task2, boolean isEquals) {
            this.task1 = task1;
            this.task2 = task2;
            this.isEquals = isEquals;
        }
    }

    @Test
    void testEquals() {

        TestExample[] examples = {
                new TestExample(
                        new EpicDto(1, "", "", TaskStatus.NEW, new ArrayList<>()),
                        new EpicDto(1, "", "", TaskStatus.NEW, new ArrayList<>()),
                        true
                ),
                new TestExample(
                        new EpicDto(1, "", "", TaskStatus.NEW, new ArrayList<>()),
                        new EpicDto(1, "2", "", TaskStatus.NEW, new ArrayList<>()),
                        false
                ),
                new TestExample(
                        new EpicDto(1, "", "", TaskStatus.NEW, new ArrayList<>()),
                        new EpicDto(1, "", "2", TaskStatus.NEW, new ArrayList<>()),
                        false
                ),
                new TestExample(
                        new EpicDto(1, "", "", TaskStatus.NEW, new ArrayList<>()),
                        new EpicDto(1, "", "", TaskStatus.DONE, new ArrayList<>()),
                        false
                ),
                new TestExample(
                        new EpicDto(1, "", "", TaskStatus.NEW, new ArrayList<>()),
                        new EpicDto(1, "", "", TaskStatus.NEW, List.of(
                                new SubtaskDto(2, "", "", TaskStatus.NEW)
                        )),
                        false
                ),
                new TestExample(
                        new EpicDto(1, "", "", TaskStatus.NEW, List.of(
                                new SubtaskDto(2, "", "", TaskStatus.NEW)
                        )),
                        new EpicDto(1, "", "", TaskStatus.NEW, List.of(
                                new SubtaskDto(2, "", "", TaskStatus.NEW)
                        )),
                        true
                ),
                new TestExample(
                        new TaskDto(1, "", "", TaskStatus.NEW),
                        new EpicDto(1, "", "", TaskStatus.NEW, new ArrayList<>()),
                        false
                ),
                new TestExample(
                        new SubtaskDto(1, "", "", TaskStatus.NEW),
                        new EpicDto(1, "", "", TaskStatus.NEW, new ArrayList<>()),
                        false
                ),
                new TestExample(
                        new SubtaskDto(1, "", "", TaskStatus.NEW),
                        new TaskDto(1, "", "", TaskStatus.NEW),
                        false
                )
        };

        for (TestExample example : examples) {
            if (example.isEquals) {
                assertEquals(example.task1, example.task2);
            } else {
                assertNotEquals(example.task1, example.task2);
            }
        }
    }
}
