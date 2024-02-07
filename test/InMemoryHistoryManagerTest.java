import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.praktikum.kanban.model.TaskStatus;
import ru.praktikum.kanban.model.dto.response.EpicDto;
import ru.praktikum.kanban.model.dto.response.SimpleTaskDto;
import ru.praktikum.kanban.model.dto.response.SubtaskDto;
import ru.praktikum.kanban.model.dto.response.TaskDto;
import ru.praktikum.kanban.service.HistoryManager;
import ru.praktikum.kanban.service.impl.InMemoryHistoryManager;
import ru.praktikum.kanban.service.impl.Managers;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    HistoryManager<TaskDto> historyManager;

    @BeforeEach
    void setUp() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void getHistory() {
        SubtaskDto subtaskDto = new SubtaskDto(1, "", "", TaskStatus.NEW);
        EpicDto epicDto = new EpicDto(1, "", "", TaskStatus.NEW, List.of());
        SimpleTaskDto simpleTaskDto = new SimpleTaskDto(1, "", "", TaskStatus.NEW);

        historyManager.add(subtaskDto);
        historyManager.add(epicDto);
        historyManager.add(simpleTaskDto);
        historyManager.add(subtaskDto);

        final List<TaskDto> history = historyManager.getHistory();
        assertEquals(
                List.of(
                        subtaskDto,
                        epicDto,
                        simpleTaskDto,
                        subtaskDto
                ),
                history
        );
    }

    @Test
    void shouldHistorySizeIs10() {

        final ArrayList<TaskDto> expected = new ArrayList<>();
        for (int i = 3; i <= 12; i++) {
            EpicDto epicDto = new EpicDto(i, "", "", TaskStatus.NEW, List.of());
            expected.add(epicDto);
        }

        for (int i = 1; i <= 12; i++) {
            EpicDto epicDto = new EpicDto(i, "", "", TaskStatus.NEW, List.of());
            historyManager.add(epicDto);
        }

        final List<TaskDto> history = historyManager.getHistory();
        assertEquals(InMemoryHistoryManager.DEFAULT_MAX_SIZE, history.size());
        assertEquals(expected, history);
    }
}
