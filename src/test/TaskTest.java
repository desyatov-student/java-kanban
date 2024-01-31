package test;

import java.util.ArrayList;
import java.util.List;
import main.model.dto.response.BaseTaskDto;
import main.model.dto.response.EpicDto;
import main.model.dto.response.SubtaskDto;
import main.model.TaskStatus;
import main.model.dto.response.TaskDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    private class TestExample {
        BaseTaskDto task1;
        BaseTaskDto task2;
        boolean expectedResult;

        public TestExample(BaseTaskDto task1, BaseTaskDto task2, boolean expectedResult) {
            this.task1 = task1;
            this.task2 = task2;
            this.expectedResult = expectedResult;
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
            assertTrue(example.task1.equals(example.task2) == example.expectedResult);
        }
    }
}
