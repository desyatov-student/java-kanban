import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.praktikum.kanban.exception.TaskFileStorageException;
import ru.praktikum.kanban.model.TasksContainer;
import ru.praktikum.kanban.model.backed.file.TasksBackup;
import ru.praktikum.kanban.model.entity.BaseTaskEntity;
import ru.praktikum.kanban.model.entity.EpicEntity;
import ru.praktikum.kanban.model.entity.SubtaskEntity;
import ru.praktikum.kanban.model.entity.TaskEntity;
import ru.praktikum.kanban.repository.impl.TaskFileStorage;
import ru.praktikum.kanban.repository.impl.TaskRepositoryBackedFile;

import static util.TaskFactory.EPIC;
import static util.TaskFactory.SUBTASK;
import static util.TaskFactory.TASK;

@ExtendWith(MockitoExtension.class)
class TaskRepositoryBackedFileTest {

    @Mock
    TaskFileStorage fileStorage;
    TaskRepositoryBackedFile repository;

    @BeforeEach
    void setup() {
        repository = new TaskRepositoryBackedFile(fileStorage);
    }

    @Test
    void shouldSaveTaskForEachUpdate() throws TaskFileStorageException {
        TasksContainer tasksContainer = new TasksContainer();
        List<BaseTaskEntity> history = new ArrayList<>();

        InOrder orderVerifier = Mockito.inOrder(fileStorage);

        EpicEntity epic1 = EPIC(1);
        EpicEntity epic2 = EPIC(2);

        SubtaskEntity subtask1 = SUBTASK(3, epic1.getId());
        SubtaskEntity subtask2 = SUBTASK(4, epic1.getId());

        TaskEntity task1 = TASK(5);
        TaskEntity task2 = TASK(6);

        repository.saveEpic(epic1);
        repository.saveEpic(epic2);
        repository.removeEpic(epic1.getId());
        repository.removeAllEpics();

        repository.saveSubtask(subtask1);
        repository.saveSubtask(subtask2);
        repository.removeSubtask(subtask1.getId());
        repository.removeAllSubtasks();

        repository.saveTask(task1);
        repository.saveTask(task2);
        repository.removeTask(task1.getId());
        repository.removeAllTasks();

        repository.addToHistory(epic1);
        repository.addToHistory(subtask1);
        repository.addToHistory(task1);
        repository.removeFromHistory(subtask1.getId());

        List<Runnable> actionsBeforeVerify = List.of(
                () -> tasksContainer.addEpic(epic1),
                () -> tasksContainer.addEpic(epic2),
                () -> { tasksContainer.epics.clear(); tasksContainer.addEpic(epic2); },
                tasksContainer.epics::clear,
                () -> tasksContainer.addSubtask(subtask1),
                () -> tasksContainer.addSubtask(subtask2),
                () -> { tasksContainer.subtasks.clear(); tasksContainer.addSubtask(subtask2); },
                tasksContainer.subtasks::clear,
                () -> tasksContainer.addTask(task1),
                () -> tasksContainer.addTask(task2),
                () -> { tasksContainer.tasks.clear(); tasksContainer.addTask(task2); },
                tasksContainer.tasks::clear,
                () -> history.add(epic1),
                () -> history.add(subtask1),
                () -> history.add(task1),
                () -> history.remove(subtask1)
        );

        for (Runnable action : actionsBeforeVerify) {
            action.run();
            orderVerifier.verify(fileStorage).save(new TasksBackup(tasksContainer, history));
        }

        Mockito.verifyNoMoreInteractions(fileStorage);
    }
}
