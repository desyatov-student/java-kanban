import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.praktikum.kanban.model.TaskStatus;
import ru.praktikum.kanban.model.entity.EpicEntity;
import ru.praktikum.kanban.model.entity.TaskEntity;
import ru.praktikum.kanban.model.entity.SubtaskEntity;
import ru.praktikum.kanban.model.entity.BaseTaskEntity;
import ru.praktikum.kanban.service.HistoryManager;
import ru.praktikum.kanban.service.impl.InMemoryHistoryManager;
import ru.praktikum.kanban.service.impl.Managers;

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
                        subtask
                ),
                history
        );
    }
}
