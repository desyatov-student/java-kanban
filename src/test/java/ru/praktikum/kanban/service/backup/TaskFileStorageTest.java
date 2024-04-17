package ru.praktikum.kanban.service.backup;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.praktikum.kanban.exception.TaskFileStorageException;
import ru.praktikum.kanban.helper.CollectionsHelper;
import ru.praktikum.kanban.model.Epic;
import ru.praktikum.kanban.model.Subtask;
import ru.praktikum.kanban.model.Task;
import ru.praktikum.kanban.service.mapper.AdvancedTaskMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.praktikum.kanban.helper.TaskFactory.EPIC;
import static ru.praktikum.kanban.helper.TaskFactory.SUBTASK;
import static ru.praktikum.kanban.helper.TaskFactory.TASK;

public class TaskFileStorageTest {

    TaskFileStorage fileStorage;

    @BeforeEach
    void setup() {
        AdvancedTaskMapper mapper = new AdvancedTaskMapper();
        TasksCsvWriter writer = new TasksCsvWriter(mapper);
        TasksCsvReader reader = new TasksCsvReader(mapper);
        fileStorage = new TaskFileStorage(writer, reader, true);
    }

    @ParameterizedTest
    @MethodSource("provideModels")
    void shouldWriteAndReadTasksOnDisk(
            TasksContainer tasksContainer,
            List<Task> history,
            List<Task> expectedHistory
    ) throws TaskFileStorageException {
        TasksBackup expectedBackup = new TasksBackup(tasksContainer, history);

        fileStorage.save(expectedBackup);
        TasksBackup actualBackup = fileStorage.getBackup();

        assertEquals(expectedBackup.getTasksList(), actualBackup.getTasksList());
        assertEquals(expectedHistory, actualBackup.getHistory());

        if (!tasksContainer.epics.isEmpty()) {
            Epic expectedEpic = tasksContainer.epics.get(5);
            Epic actualEpic = actualBackup.tasksContainer.epics.get(5);
            assertEquals(expectedEpic.getId(), actualEpic.getId());
            assertEquals(expectedEpic.getName(), actualEpic.getName());
            assertEquals(expectedEpic.getDescription(), actualEpic.getDescription());
            assertEquals(expectedEpic.getStatus(), actualEpic.getStatus());
            assertEquals(expectedEpic.getStartTime(), actualEpic.getStartTime());
            assertEquals(expectedEpic.getDuration(), actualEpic.getDuration());
            assertEquals(expectedEpic.getEndTime(), actualEpic.getEndTime());
            assertEquals(expectedEpic.subtasks, actualEpic.subtasks);

            assertNotNull(actualEpic.getStartTime());
            assertNotNull(actualEpic.getDuration());
            assertNotNull(actualEpic.getEndTime());

            Task expectedTask = tasksContainer.tasks.get(3);
            Task actualTask = actualBackup.tasksContainer.tasks.get(3);
            assertEquals(expectedTask.getId(), actualTask.getId());
            assertEquals(expectedTask.getName(), actualTask.getName());
            assertEquals(expectedTask.getDescription(), actualTask.getDescription());
            assertEquals(expectedTask.getStatus(), actualTask.getStatus());
            assertEquals(expectedTask.getStartTime(), actualTask.getStartTime());
            assertEquals(expectedTask.getDuration(), actualTask.getDuration());
            assertEquals(expectedTask.getEndTime(), actualTask.getEndTime());

            assertNotNull(actualTask.getStartTime());
            assertNotNull(actualTask.getDuration());
            assertNotNull(actualTask.getEndTime());

            Subtask expectedSubtask = tasksContainer.subtasks.get(7);
            Subtask actualSubtask = actualBackup.tasksContainer.subtasks.get(7);
            assertEquals(expectedSubtask.getId(), actualSubtask.getId());
            assertEquals(expectedSubtask.getName(), actualSubtask.getName());
            assertEquals(expectedSubtask.getDescription(), actualSubtask.getDescription());
            assertEquals(expectedSubtask.getStatus(), actualSubtask.getStatus());
            assertEquals(expectedSubtask.getStartTime(), actualSubtask.getStartTime());
            assertEquals(expectedSubtask.getDuration(), actualSubtask.getDuration());
            assertEquals(expectedSubtask.getEndTime(), actualSubtask.getEndTime());

            assertNotNull(actualSubtask.getStartTime());
            assertNotNull(actualSubtask.getDuration());
            assertNotNull(actualSubtask.getEndTime());
        }
    }

    private static Stream<Arguments> provideModels() {
        LocalDateTime startTime = LocalDateTime.now();
        Duration duration = Duration.ofMinutes(10);
        LocalDateTime endTime = startTime.plus(duration);
        return Stream.of(
                Arguments.of(
                        CollectionsHelper.tasksListsToContainer(
                                List.of(EPIC(5, List.of(6, 7), startTime, duration, endTime)),
                                List.of(SUBTASK(6, 5), SUBTASK(7, 5, startTime, duration)),
                                List.of(TASK(1), TASK(2), TASK(3, startTime, duration))
                        ),
                        List.of(TASK(1), TASK(4)),
                        List.of(TASK(1))
                ),
                Arguments.of(
                        CollectionsHelper.tasksListsToContainer(
                                List.of(),
                                List.of(),
                                List.of()
                        ),
                        List.of(),
                        List.of()
                ),
                Arguments.of(
                        CollectionsHelper.tasksListsToContainer(
                                List.of(),
                                List.of(),
                                List.of(TASK(1), TASK(2), TASK(3))
                        ),
                        List.of(),
                        List.of()
                )
        );
    }
}
