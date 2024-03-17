import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.praktikum.kanban.model.entity.BaseTaskEntity;
import ru.praktikum.kanban.model.entity.EpicEntity;
import ru.praktikum.kanban.model.entity.SubtaskEntity;
import ru.praktikum.kanban.model.entity.TaskEntity;
import ru.praktikum.kanban.repository.HistoryRepository;
import ru.praktikum.kanban.repository.impl.TaskRepositoryInMemory;
import ru.praktikum.kanban.service.HistoryManager;
import ru.praktikum.kanban.service.impl.HistoryManagerImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static util.TaskFactory.EPIC;
import static util.TaskFactory.SUBTASK;
import static util.TaskFactory.TASK;

class HistoryManagerImplTest {

    HistoryManager historyManager;

    HistoryRepository repository;

    @BeforeEach
    void setUp() {
        repository = Mockito.spy(TaskRepositoryInMemory.class);
        historyManager = new HistoryManagerImpl(repository);
    }

    @Test
    void getHistory() {
        SubtaskEntity subtask = SUBTASK(1);
        EpicEntity epic = EPIC(1);
        TaskEntity task = TASK(1);

        historyManager.add(subtask);
        historyManager.add(epic);
        historyManager.add(task);
        historyManager.add(subtask);

        final List<BaseTaskEntity> history = historyManager.getHistory();
        assertEquals(
                List.of(
                        subtask
                ),
                history
        );
    }

    @Test
    void shouldRemoveTask() {
        TaskEntity task = TASK(1);
        historyManager.add(task);
        historyManager.remove(task.getId());
        historyManager.add(null);

        Mockito.verify(repository).addToHistory(task);
        Mockito.verify(repository).removeFromHistory(task.getId());
        Mockito.verifyNoMoreInteractions(repository);
    }
}
