package ru.praktikum.kanban.model;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import ru.praktikum.kanban.dto.EpicDto;
import ru.praktikum.kanban.dto.SubtaskDto;
import ru.praktikum.kanban.dto.TaskDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static ru.praktikum.kanban.helper.TaskFactory.EPIC_DTO;
import static ru.praktikum.kanban.helper.TaskFactory.SUBTASK_DTO;
import static ru.praktikum.kanban.helper.TaskFactory.TASK_DTO;

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
                        EPIC_DTO(1),
                        EPIC_DTO(1),
                        true
                ),
                new TestExample(
                        EPIC_DTO(1),
                        EPIC_DTO(1, "2", null),
                        false
                ),
                new TestExample(
                        EPIC_DTO(1),
                        EPIC_DTO(1, null, "2"),
                        false
                ),
                new TestExample(
                        EPIC_DTO(1),
                        EPIC_DTO(1, TaskStatus.DONE),
                        false
                ),
                new TestExample(
                        EPIC_DTO(1),
                        EPIC_DTO(1, List.of(
                                SUBTASK_DTO(2)
                        )),
                        false
                ),
                new TestExample(
                        EPIC_DTO(1, List.of(
                                SUBTASK_DTO(2)
                        )),
                        EPIC_DTO(1, List.of(
                                SUBTASK_DTO(2)
                        )),
                        true
                ),
                new TestExample(
                        TASK_DTO(1),
                        EPIC_DTO(1),
                        false
                ),
                new TestExample(
                        SUBTASK_DTO(1),
                        EPIC_DTO(1),
                        false
                ),
                new TestExample(
                        SUBTASK_DTO(1),
                        TASK_DTO(1),
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
