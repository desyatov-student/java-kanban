package ru.praktikum.kanban.service.impl;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.praktikum.kanban.model.Task;
import ru.praktikum.kanban.model.Epic;
import ru.praktikum.kanban.model.Subtask;
import ru.praktikum.kanban.repository.HistoryRepository;
import ru.praktikum.kanban.repository.impl.InMemoryTaskRepository;
import ru.praktikum.kanban.service.HistoryManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.praktikum.kanban.helper.TaskFactory.EPIC;
import static ru.praktikum.kanban.helper.TaskFactory.SUBTASK;
import static ru.praktikum.kanban.helper.TaskFactory.TASK;

class HistoryManagerImplTest {

    HistoryManager historyManager;

    HistoryRepository repository;

    @BeforeEach
    void setUp() {
        repository = Mockito.spy(InMemoryTaskRepository.class);
        historyManager = new HistoryManagerImpl(repository);
    }

    @Test
    void getHistory() {
        Subtask subtask = SUBTASK(1);
        Epic epic = EPIC(1);
        Task task = TASK(1);

        historyManager.add(subtask);
        historyManager.add(epic);
        historyManager.add(task);
        historyManager.add(subtask);

        final List<Task> history = historyManager.getHistory();
        assertEquals(
                List.of(
                        subtask
                ),
                history
        );
    }

    @Test
    void shouldRemoveTask() {
        Task task = TASK(1);
        historyManager.add(task);
        historyManager.remove(task.getId());
        historyManager.add(null);

        Mockito.verify(repository).addToHistory(task);
        Mockito.verify(repository).removeFromHistory(task.getId());
        Mockito.verifyNoMoreInteractions(repository);
    }
}
