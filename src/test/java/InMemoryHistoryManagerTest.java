import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Spy;
import ru.praktikum.kanban.model.HistoryLinkedList;
import ru.praktikum.kanban.model.TaskStatus;
import ru.praktikum.kanban.model.dto.response.BaseTaskDto;
import ru.praktikum.kanban.model.dto.response.EpicDto;
import ru.praktikum.kanban.model.dto.response.SubtaskDto;
import ru.praktikum.kanban.model.dto.response.TaskDto;
import ru.praktikum.kanban.service.HistoryManager;
import ru.praktikum.kanban.service.impl.InMemoryHistoryManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {

    HistoryManager historyManager;

    @Spy
    HistoryLinkedList historyLinkedList;

    @BeforeEach
    void setUp() {
        historyLinkedList = Mockito.spy(HistoryLinkedList.class);
        historyManager = new InMemoryHistoryManager(historyLinkedList);
    }

    @Test
    void getHistory() {
        SubtaskDto subtask = new SubtaskDto(1, "", "", TaskStatus.NEW);
        EpicDto epic = new EpicDto(1, "", "", TaskStatus.NEW, List.of());
        TaskDto taskDto = new TaskDto(1, "", "", TaskStatus.NEW);

        historyManager.add(subtask);
        historyManager.add(epic);
        historyManager.add(taskDto);
        historyManager.add(subtask);

        final List<BaseTaskDto> history = historyManager.getHistory();
        assertEquals(
                List.of(
                        subtask
                ),
                history
        );
    }

    @Test
    void shouldRemoveTask() {
        TaskDto taskDto = new TaskDto(1, "", "", TaskStatus.NEW);
        historyManager.add(taskDto);
        historyManager.remove(taskDto.getId());
        historyManager.add(null);

        Mockito.verify(historyLinkedList).add(taskDto);
        Mockito.verify(historyLinkedList).remove(taskDto.getId());
        Mockito.verifyNoMoreInteractions(historyLinkedList);
    }
}
