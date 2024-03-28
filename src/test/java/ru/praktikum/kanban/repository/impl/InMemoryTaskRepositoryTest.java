package ru.praktikum.kanban.repository.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
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
        DataGenerator generator = new DataGenerator();
        IdentifierGenerator identifier = new IdentifierGenerator();

        Epic epic1 = EPIC(identifier.getNextId(), List.of(), generator.startTime, generator.duration, generator.endTime);
        generator = generator.getNext();
        Epic epic2 = EPIC(identifier.getNextId(), List.of(), generator.startTime, generator.duration, generator.endTime);
        Epic epic3 = EPIC(identifier.getNextId(), List.of());

        generator = generator.getNext();
        Subtask subtask1 = SUBTASK(identifier.getNextId(), epic1.getId(), generator.startTime, generator.duration);
        generator = generator.getNext();
        Subtask subtask2 = SUBTASK(identifier.getNextId(), epic1.getId(), generator.startTime, generator.duration);
        Subtask subtask3 = SUBTASK(identifier.getNextId(), epic1.getId());

        generator = generator.getNext();
        Task task1 = TASK(identifier.getNextId(), generator.startTime, generator.duration);
        generator = generator.getNext();
        Task task2 = TASK(identifier.getNextId(), generator.startTime, generator.duration);
        Task task3 = TASK(identifier.getNextId());

        List<Task> expectedTasks =List.of(epic1, epic2, subtask1, subtask2, task1, task2);

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

    static class DataGenerator {
        LocalDateTime startTime;
        Duration duration;
        LocalDateTime endTime;

        public DataGenerator() {
            this.startTime = LocalDateTime.now();
            this.duration = Duration.ofMinutes(30);
            this.endTime = startTime.plus(duration);
        }

        public DataGenerator(LocalDateTime startTime, Duration duration, LocalDateTime endTime) {
            this.startTime = startTime;
            this.duration = duration;
            this.endTime = endTime;
        }

        DataGenerator getNext() {
            LocalDateTime newStartTime = this.startTime.plusDays(1);
            return new DataGenerator(newStartTime, duration, newStartTime.plus(duration));
        }
    }
}
