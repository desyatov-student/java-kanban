package ru.praktikum.kanban;

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
import ru.praktikum.kanban.repository.impl.FileBackedTaskRepository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.praktikum.kanban.util.TaskFactory.EPIC;
import static ru.praktikum.kanban.util.TaskFactory.SUBTASK;
import static ru.praktikum.kanban.util.TaskFactory.TASK;

@ExtendWith(MockitoExtension.class)
class FileBackedTaskRepositoryTest {

    @Mock
    TaskFileStorage fileStorage;
    FileBackedTaskRepository repository;

    @BeforeEach
    void setup() {
        repository = new FileBackedTaskRepository(fileStorage);
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

    @Test
    void shouldHandleThrowsForSave() throws TaskFileStorageException {
        Mockito.doThrow(new TaskFileStorageException()).when(fileStorage).save(Mockito.any());
        assertDoesNotThrow(() -> repository.saveTask(TASK(1)));
    }

    @Test
    void shouldThrowsErrorForSave() throws TaskFileStorageException {
        Mockito.doThrow(new RuntimeException()).when(fileStorage).save(Mockito.any());
        assertThrows(RuntimeException.class, () -> repository.saveTask(TASK(1)));
    }

    @Test
    void shouldReturnBackup() throws TaskFileStorageException {

        EpicEntity epic1 = EPIC(1);
        EpicEntity epic2 = EPIC(2);

        SubtaskEntity subtask1 = SUBTASK(3, epic1.getId());
        SubtaskEntity subtask2 = SUBTASK(4, epic1.getId());

        TaskEntity task1 = TASK(5);
        TaskEntity task2 = TASK(6);

        TasksContainer tasksContainer = new TasksContainer();
        tasksContainer.addEpic(epic1);
        tasksContainer.addEpic(epic2);
        tasksContainer.addSubtask(subtask1);
        tasksContainer.addSubtask(subtask2);
        tasksContainer.addTask(task1);
        tasksContainer.addTask(task2);

        List<BaseTaskEntity> history = List.of(epic1, subtask1, task1);

        TasksBackup backup = new TasksBackup(tasksContainer, history);
        Mockito.when(fileStorage.getBackup()).thenReturn(backup);

        assertTrue(repository.getAllEpics().isEmpty());
        assertTrue(repository.getAllSubtasks().isEmpty());
        assertTrue(repository.getAllTasks().isEmpty());
        assertTrue(repository.getHistory().isEmpty());

        repository.loadFromFileStorage();
        repository.loadFromFileStorage();

        Mockito.verify(fileStorage, Mockito.times(1)).getBackup();

        assertEquals(List.of(epic1, epic2), repository.getAllEpics());
        assertEquals(List.of(subtask1, subtask2), repository.getAllSubtasks());
        assertEquals(List.of(task1, task2), repository.getAllTasks());
        assertEquals(history, repository.getHistory());

        Mockito.verifyNoMoreInteractions(fileStorage);
    }

    @Test
    void loadFromFileStorage_catchTaskFileStorageException_getBackupThrowTaskFileStorageException()
            throws TaskFileStorageException {
        //given
        Mockito.doThrow(new TaskFileStorageException()).when(fileStorage).getBackup();
        //then
        assertDoesNotThrow(() -> repository.loadFromFileStorage());
    }

    @Test
    void loadFromFileStorage_throwRuntimeException_getBackupThrowRuntimeException() throws TaskFileStorageException {
        //given
        Mockito.doThrow(new RuntimeException()).when(fileStorage).getBackup();
        //then
        assertThrows(RuntimeException.class, () -> repository.loadFromFileStorage());
    }
}
