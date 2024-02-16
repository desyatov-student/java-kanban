import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.java.ru.praktikum.kanban.model.TaskStatus;
import main.java.ru.praktikum.kanban.model.entity.EpicEntity;
import main.java.ru.praktikum.kanban.model.entity.TaskEntity;
import main.java.ru.praktikum.kanban.model.entity.SubtaskEntity;
import main.java.ru.praktikum.kanban.model.entity.BaseTaskEntity;
import main.java.ru.praktikum.kanban.service.HistoryManager;
import main.java.ru.praktikum.kanban.service.impl.InMemoryHistoryManager;
import main.java.ru.praktikum.kanban.service.impl.Managers;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {

    HistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void getHistory() {
        SubtaskEntity subtask = new SubtaskEntity(1, "", "", TaskStatus.NEW);
        EpicEntity epic = new EpicEntity(1, "", "", TaskStatus.NEW);
        TaskEntity simpleTaskDto = new TaskEntity(1, "", "", TaskStatus.NEW);

        historyManager.add(subtask);
        historyManager.add(epic);
        historyManager.add(simpleTaskDto);
        historyManager.add(subtask);

        final List<BaseTaskEntity> history = historyManager.getHistory();
        assertEquals(
                List.of(
                        subtask,
                        epic,
                        simpleTaskDto,
                        subtask
                ),
                history
        );
    }

    @Test
    void shouldHistorySizeIs10() {

        final ArrayList<BaseTaskEntity> expected = new ArrayList<>();
        for (int i = 3; i <= 12; i++) {
            EpicEntity epic = new EpicEntity(i, "", "", TaskStatus.NEW);
            expected.add(epic);
        }

        for (int i = 1; i <= 12; i++) {
            EpicEntity epic = new EpicEntity(i, "", "", TaskStatus.NEW);
            historyManager.add(epic);
        }

        final List<BaseTaskEntity> history = historyManager.getHistory();
        assertEquals(InMemoryHistoryManager.DEFAULT_MAX_SIZE, history.size());
        assertEquals(expected, history);
    }
}
