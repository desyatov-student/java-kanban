package test;

import main.models.Epic;
import main.models.Subtask;
import main.models.Task;
import main.models.TaskStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TaskTest {

    @Test
    void testEquals() {
        Task task1 = new Task(1, "", "", TaskStatus.NEW);
        Task task2 = new Task(1, "", "", TaskStatus.NEW);
        assertEquals(task1, task2);

        Task epic1 = new Epic(1, "", "", TaskStatus.NEW);
        Task epic2 = new Epic(1, "", "", TaskStatus.NEW);
        assertEquals(epic1, epic2);


        assertEquals(epic1, epic2);
    }

    @Test
    void testNotEquals() {
        Task task = new Task(1, "", "", TaskStatus.NEW);
        Task epic = new Epic(1, "", "", TaskStatus.NEW);
        Task subtask = new Subtask(1, "", "", TaskStatus.NEW);
        assertNotEquals(task, epic);
        assertNotEquals(subtask, epic);
        assertNotEquals(subtask, task);
    }
}
