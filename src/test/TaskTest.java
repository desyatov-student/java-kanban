package test;

import java.util.ArrayList;
import main.models.BaseTask;
import main.models.dto.EpicDto;
import main.models.dto.SubtaskDto;
import main.models.TaskStatus;
import main.models.dto.TaskDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TaskTest {

    @Test
    void testEquals() {
        BaseTask task1 = new TaskDto(1, "", "", TaskStatus.NEW);
        BaseTask task2 = new TaskDto(1, "", "", TaskStatus.NEW);
        assertEquals(task1, task2);

        BaseTask epic1 = new EpicDto(1, "", "", TaskStatus.NEW, new ArrayList<>());
        BaseTask epic2 = new EpicDto(1, "", "", TaskStatus.NEW, new ArrayList<>());
        assertEquals(epic1, epic2);


        assertEquals(epic1, epic2);
    }

    @Test
    void testNotEquals() {
        BaseTask task = new TaskDto(1, "", "", TaskStatus.NEW);
        EpicDto epic = new EpicDto(1, "", "", TaskStatus.NEW, new ArrayList<>());
        BaseTask subtask = new SubtaskDto(1, "", "", TaskStatus.NEW, epic);
        assertNotEquals(task, epic);
        assertNotEquals(subtask, epic);
        assertNotEquals(subtask, task);
    }
}
