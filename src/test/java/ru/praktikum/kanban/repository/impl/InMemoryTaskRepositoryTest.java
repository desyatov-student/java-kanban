package ru.praktikum.kanban.repository.impl;

import java.util.List;
import org.junit.jupiter.api.Test;
import ru.praktikum.kanban.helper.StartTimeGenerator;
import ru.praktikum.kanban.model.Epic;
import ru.praktikum.kanban.model.Subtask;
import ru.praktikum.kanban.model.Task;
import ru.praktikum.kanban.util.IdentifierGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.praktikum.kanban.helper.TaskFactory.EPIC;
import static ru.praktikum.kanban.helper.TaskFactory.SUBTASK;
import static ru.praktikum.kanban.helper.TaskFactory.TASK;

public class InMemoryTaskRepositoryTest {

    @Test
    void getPrioritizedTasksShouldReturnTasksSortedByStartTimeWhenSaveTasksWithStartTime() {
        // given
        InMemoryTaskRepository repository = new InMemoryTaskRepository();
        StartTimeGenerator generator = new StartTimeGenerator();
        IdentifierGenerator identifier = new IdentifierGenerator();

        Epic epic1 = EPIC(identifier.getNextId(), List.of(), generator.getStartTime(), generator.getDuration(), generator.getEndTime());
        Epic epic2 = EPIC(identifier.getNextId(), List.of(), generator.getStartTime(), generator.getDuration(), generator.getEndTime());
        Epic epic3 = EPIC(identifier.getNextId(), List.of());

        Subtask subtask1 = SUBTASK(identifier.getNextId(), epic1.getId(), generator.getStartTime(), generator.getDuration());
        Subtask subtask2 = SUBTASK(identifier.getNextId(), epic1.getId(), generator.getStartTime(), generator.getDuration());
        Subtask subtask3 = SUBTASK(identifier.getNextId(), epic1.getId());

        Task task1 = TASK(identifier.getNextId(), generator.getStartTime(), generator.getDuration());
        Task task2 = TASK(identifier.getNextId(), generator.getStartTime(), generator.getDuration());
        Task task3 = TASK(identifier.getNextId());

        List<Task> expectedTasks =List.of(subtask1, subtask2, task1, task2);

        // when

        repository.saveTask(task3);
        repository.saveTask(task2);
        repository.saveTask(task1);
        repository.saveEpic(epic3);
        repository.saveEpic(epic2);
        repository.saveEpic(epic1);
        repository.saveSubtask(subtask3);
        repository.saveSubtask(subtask2);
        repository.saveSubtask(subtask1);

        // then

        assertEquals(expectedTasks, repository.getPrioritizedTasks());

    }
}
