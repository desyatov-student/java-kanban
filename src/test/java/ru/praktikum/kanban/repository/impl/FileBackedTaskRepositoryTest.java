package ru.praktikum.kanban.repository.impl;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.praktikum.kanban.exception.TaskFileStorageException;
import ru.praktikum.kanban.helper.StartTimeGenerator;
import ru.praktikum.kanban.model.Epic;
import ru.praktikum.kanban.model.Subtask;
import ru.praktikum.kanban.model.Task;
import ru.praktikum.kanban.service.backup.TaskFileStorage;
import ru.praktikum.kanban.service.backup.TasksBackup;
import ru.praktikum.kanban.service.backup.TasksContainer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.praktikum.kanban.helper.TaskFactory.EPIC;
import static ru.praktikum.kanban.helper.TaskFactory.SUBTASK;
import static ru.praktikum.kanban.helper.TaskFactory.TASK;

@ExtendWith(MockitoExtension.class)
class FileBackedTaskRepositoryTest {

    @Mock
    TaskFileStorage fileStorage;

    @Test
    void shouldSaveTaskForEachUpdate() throws TaskFileStorageException {
        //given
        TasksContainer tasksContainer = new TasksContainer();
        List<Task> history = new ArrayList<>();

        InOrder orderVerifier = Mockito.inOrder(fileStorage);

        Epic epic1 = EPIC(1);
        Epic epic2 = EPIC(2);

        Subtask subtask1 = SUBTASK(3, epic1.getId());
        Subtask subtask2 = SUBTASK(4, epic1.getId());

        Task task1 = TASK(5);
        Task task2 = TASK(6);

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
        Mockito.when(fileStorage.getBackup()).thenReturn(new TasksBackup());
        FileBackedTaskRepository repository = new FileBackedTaskRepository(fileStorage);

        //when

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

        //then
        for (Runnable action : actionsBeforeVerify) {
            action.run();
            orderVerifier.verify(fileStorage).save(new TasksBackup(tasksContainer, history));
        }

        Mockito.verifyNoMoreInteractions(fileStorage);
    }

    @Test
    void shouldHandleThrowsForSave() throws TaskFileStorageException {
        //given
        Mockito.when(fileStorage.getBackup()).thenReturn(new TasksBackup());
        FileBackedTaskRepository repository = new FileBackedTaskRepository(fileStorage);
        Mockito.doThrow(new TaskFileStorageException()).when(fileStorage).save(Mockito.any());
        //then
        assertDoesNotThrow(() -> repository.saveTask(TASK(1)));
    }

    @Test
    void shouldThrowsErrorForSave() throws TaskFileStorageException {
        //given
        Mockito.when(fileStorage.getBackup()).thenReturn(new TasksBackup());
        FileBackedTaskRepository repository = new FileBackedTaskRepository(fileStorage);
        Mockito.doThrow(new RuntimeException()).when(fileStorage).save(Mockito.any());
        //then
        assertThrows(RuntimeException.class, () -> repository.saveTask(TASK(1)));
    }

    @Test
    void shouldReturnBackup() throws TaskFileStorageException {
        // given

        StartTimeGenerator generator = new StartTimeGenerator();

        Epic epic1 = EPIC(5, List.of(), generator.getStartTime(), generator.getDuration(), generator.getEndTime());
        Epic epic2 = EPIC(6);

        Subtask subtask1 = SUBTASK(3, epic1.getId(), generator.getStartTime(), generator.getDuration());
        Subtask subtask2 = SUBTASK(4, epic1.getId());

        Task task1 = TASK(1, generator.getStartTime(), generator.getDuration());
        Task task2 = TASK(2);

        TasksContainer tasksContainer = new TasksContainer();
        tasksContainer.addEpic(epic1);
        tasksContainer.addEpic(epic2);
        tasksContainer.addSubtask(subtask1);
        tasksContainer.addSubtask(subtask2);
        tasksContainer.addTask(task1);
        tasksContainer.addTask(task2);

        List<Task> history = List.of(epic1, subtask1, task1);

        TasksBackup backup = new TasksBackup(tasksContainer, history);
        Mockito.when(fileStorage.getBackup()).thenReturn(backup);

        //when
        FileBackedTaskRepository repository = new FileBackedTaskRepository(fileStorage);

        //then
        Mockito.verify(fileStorage, Mockito.times(1)).getBackup();
        assertEquals(List.of(epic1, epic2), repository.getAllEpics());
        assertEquals(List.of(subtask1, subtask2), repository.getAllSubtasks());
        assertEquals(List.of(task1, task2), repository.getAllTasks());
        assertEquals(history, repository.getHistory());

        Task actualEpic = repository.getAllEpics().get(0);
        assertEquals(epic1.getStartTime(), actualEpic.getStartTime());
        assertEquals(epic1.getDuration(), actualEpic.getDuration());
        assertEquals(epic1.getEndTime(), actualEpic.getEndTime());

        Mockito.verifyNoMoreInteractions(fileStorage);

        assertEquals(2, repository.prioritizedTasks.size());

        List<Task> actualPrioritizedTasks = new ArrayList<>(repository.prioritizedTasks);

        Task actualSubtask = actualPrioritizedTasks.get(0);
        Task actualTask = actualPrioritizedTasks.get(1);

        assertEquals(subtask1.getStartTime(), actualSubtask.getStartTime());
        assertEquals(subtask1.getDuration(), actualSubtask.getDuration());
        assertEquals(subtask1.getEndTime(), actualSubtask.getEndTime());

        assertEquals(task1.getStartTime(), actualTask.getStartTime());
        assertEquals(task1.getDuration(), actualTask.getDuration());
        assertEquals(task1.getEndTime(), actualTask.getEndTime());

    }

    @Test
    void getBackupShouldReturnEmptyTasksWhenFileStorageIsEmpty() throws TaskFileStorageException {
        //given

        TasksContainer tasksContainer = new TasksContainer();
        List<Task> history = List.of();
        TasksBackup backup = new TasksBackup(tasksContainer, history);
        Mockito.when(fileStorage.getBackup()).thenReturn(backup);

        //when
        FileBackedTaskRepository repository = new FileBackedTaskRepository(fileStorage);

        //then
        assertTrue(repository.getAllEpics().isEmpty());
        assertTrue(repository.getAllSubtasks().isEmpty());
        assertTrue(repository.getAllTasks().isEmpty());
        assertTrue(repository.getHistory().isEmpty());

        Mockito.verify(fileStorage, Mockito.times(1)).getBackup();
        Mockito.verifyNoMoreInteractions(fileStorage);
    }

    @Test
    void loadFromFileStorage_catchTaskFileStorageException_getBackupThrowTaskFileStorageException()
            throws TaskFileStorageException {
        //given
        Mockito.doThrow(new TaskFileStorageException()).when(fileStorage).getBackup();
        //then
        assertDoesNotThrow(() -> new FileBackedTaskRepository(fileStorage));
    }

    @Test
    void loadFromFileStorage_throwRuntimeException_getBackupThrowRuntimeException() throws TaskFileStorageException {
        //given
        Mockito.doThrow(new RuntimeException()).when(fileStorage).getBackup();
        //then
        assertThrows(RuntimeException.class, () -> new FileBackedTaskRepository(fileStorage));
    }
}
