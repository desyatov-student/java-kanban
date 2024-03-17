import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.praktikum.kanban.exception.TaskFileStorageException;
import ru.praktikum.kanban.model.TaskType;
import ru.praktikum.kanban.model.TasksContainer;
import ru.praktikum.kanban.model.backed.file.TasksBackup;
import ru.praktikum.kanban.model.entity.BaseTaskEntity;
import ru.praktikum.kanban.model.mapper.BaseTaskEntityMapper;
import ru.praktikum.kanban.model.mapper.CollectionsHelper;
import ru.praktikum.kanban.repository.impl.TaskFileStorage;
import ru.praktikum.kanban.repository.impl.TasksCsvReader;
import ru.praktikum.kanban.repository.impl.TasksCsvWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static util.TaskFactory.EPIC;
import static util.TaskFactory.SUBTASK;
import static util.TaskFactory.TASK;

public class TaskFileStorageTest {

    TaskFileStorage fileStorage;

    @BeforeEach
    void setup() {
        BaseTaskEntityMapper mapper = new BaseTaskEntityMapper();
        TasksCsvWriter writer = new TasksCsvWriter(mapper);
        TasksCsvReader reader = new TasksCsvReader(mapper);
        fileStorage = new TaskFileStorage(writer, reader, true);
    }

    @ParameterizedTest
    @MethodSource("provideModels")
    void shouldWriteAndReadTasksOnDisk(
            TasksContainer tasksContainer,
            List<BaseTaskEntity> history,
            List<BaseTaskEntity> expectedHistory
    ) throws TaskFileStorageException {
        TasksBackup expectedBackup = new TasksBackup(tasksContainer, history);

        fileStorage.save(expectedBackup);
        TasksBackup actualBackup = fileStorage.getBackup();

        assertEquals(expectedBackup.getTasksList(), actualBackup.getTasksList());
        assertEquals(expectedHistory, actualBackup.getHistory());
    }

    private static Stream<Arguments> provideModels() {
        return Stream.of(
                Arguments.of(
                        CollectionsHelper.tasksListsToContainer(
                                List.of(EPIC(5, List.of(6))),
                                List.of(SUBTASK(6, 5), SUBTASK(7, 5)),
                                List.of(TASK(1), TASK(2), TASK(3))
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
