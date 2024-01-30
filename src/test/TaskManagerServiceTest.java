package test;

import java.util.List;
import main.models.TaskStatus;
import main.models.dto.CreateEpicDto;
import main.models.dto.CreateSubtaskDto;
import main.models.dto.EpicDto;
import main.models.dto.SubtaskDto;
import main.models.dto.UpdateSubtaskDto;
import main.repository.TaskRepositoryInMemory;
import main.utils.IdentifierGenerator;
import main.utils.MappingUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.service.TaskManagerService;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerServiceTest {

    TaskManagerService taskManager;

    @Test
    void getNextTaskId() {
        TaskManagerService taskManager = taskManagerService();
        assertEquals(IdentifierGenerator.INITIAL_IDENTIFIER, taskManager.getNextTaskId());
        taskManager.getNextTaskId();
        taskManager.getNextTaskId();
        taskManager.getNextTaskId();
        assertEquals(5, taskManager.getNextTaskId());
    }

    @BeforeEach
    void setUp() {
        taskManager = taskManagerService();
    }

    @Test
    void getAllEmpty() {
        assertTrue(taskManager.getAllEpics().isEmpty());
        assertTrue(taskManager.getAllSubtasks().isEmpty());
        assertTrue(taskManager.getAllTasks().isEmpty());
    }
    @Test
    void createEpic() {
        setupFirstEpic();

        assertEquals(
                List.of(
                        EPIC_WITHOUT_SUBTASKS1
                ),
                taskManager.getAllEpics()
        );
    }

    @Test
    void createSubtask() {

        EpicDto epicDto = setupFirstEpic();
        taskManager.createSubtask(CREATE_SUBTASK_1, epicDto.getId());

        assertEquals(
                List.of(
                        EPIC_WITH_SUBTASKS1
                ),
                taskManager.getAllEpics()
        );
    }

    @Test
    void updateSubtask() {

        EpicDto epicDto = setupFirstEpicWithSubtask1(CREATE_SUBTASK_1);

        taskManager.updateSubtask(UPDATE_SUBTASK_1_DONE);

        assertEquals(
                List.of(
                        EPIC_UPDATE_SUBTASKS1_DONE
                ),
                taskManager.getAllEpics()
        );

        taskManager.createSubtask(CREATE_SUBTASK_2, epicDto.getId());

        assertEquals(
                List.of(
                        EPIC_UPDATE_SUBTASKS_1_2_IN_PG
                ),
                taskManager.getAllEpics()
        );

        taskManager.updateSubtask(UPDATE_SUBTASK_2_DONE);

        assertEquals(
                List.of(
                        EPIC_SUBTASKS_1DONE_2DONE
                ),
                taskManager.getAllEpics()
        );

        taskManager.updateSubtask(UPDATE_SUBTASK_1_NEW);
        taskManager.updateSubtask(UPDATE_SUBTASK_2_NEW);

        assertEquals(
                List.of(
                        EPIC_UPDATE_SUBTASKS_1_2_NEW
                ),
                taskManager.getAllEpics()
        );

        taskManager.updateSubtask(UPDATE_SUBTASK_1_NEW);
        taskManager.updateSubtask(UPDATE_SUBTASK_2_IN_PG);

        assertEquals(
                List.of(
                        EPIC_UPDATE_SUBTASKS_1NEW_2IN_PG
                ),
                taskManager.getAllEpics()
        );
    }

    @Test
    void removeSubtask() {
        EpicDto epicDto = setupFirstEpicWithSubtask1(CREATE_SUBTASK_1);
        taskManager.createSubtask(CREATE_SUBTASK_2, epicDto.getId());
        SubtaskDto subtaskDto = taskManager.createSubtask(CREATE_SUBTASK_3, epicDto.getId());

        taskManager.updateSubtask(UPDATE_SUBTASK_1_DONE);
        taskManager.updateSubtask(UPDATE_SUBTASK_2_DONE);

        assertEquals(
                List.of(
                        EPIC_SUBTASKS_1DONE_2DONE_3NEW
                ),
                taskManager.getAllEpics()
        );

        taskManager.removeSubtask(subtaskDto.getId());

        assertEquals(
                List.of(
                        EPIC_SUBTASKS_1DONE_2DONE
                ),
                taskManager.getAllEpics()
        );
    }

    private EpicDto setupFirstEpic() {
        return taskManager.createEpic(CREATE_EPIC1);
    }

    private EpicDto setupFirstEpicWithSubtask1(CreateSubtaskDto createSubtaskDto) {
        EpicDto epicDto = setupFirstEpic();
        taskManager.createSubtask(createSubtaskDto, epicDto.getId());
        return taskManager.getAllEpics().get(0);
    }

    private final CreateEpicDto CREATE_EPIC1 = new CreateEpicDto(1, "Epic1", "EpicDesc1");
    private final EpicDto EPIC_WITHOUT_SUBTASKS1 = new EpicDto(1, "Epic1", "EpicDesc1", TaskStatus.NEW, List.of());
    private final CreateSubtaskDto CREATE_SUBTASK_1 = new CreateSubtaskDto(2, "ST1", "STD1");
    private final CreateSubtaskDto CREATE_SUBTASK_2 = new CreateSubtaskDto(3, "ST2", "STD2");
    private final CreateSubtaskDto CREATE_SUBTASK_3 = new CreateSubtaskDto(4, "ST3", "STD3");
    private final UpdateSubtaskDto UPDATE_SUBTASK_1_DONE = new UpdateSubtaskDto(2, "UST1", "USTD1", TaskStatus.DONE);
    private final UpdateSubtaskDto UPDATE_SUBTASK_1_NEW = new UpdateSubtaskDto(2, "UST1", "USTD1", TaskStatus.NEW);
    private final UpdateSubtaskDto UPDATE_SUBTASK_2_DONE = new UpdateSubtaskDto(3, "UST2", "USTD2", TaskStatus.DONE);
    private final UpdateSubtaskDto UPDATE_SUBTASK_2_NEW = new UpdateSubtaskDto(3, "UST2", "USTD2", TaskStatus.NEW);
    private final UpdateSubtaskDto UPDATE_SUBTASK_2_IN_PG = new UpdateSubtaskDto(3, "UST2", "USTD2", TaskStatus.IN_PROGRESS);
    private final EpicDto EPIC_WITH_SUBTASKS1 = new EpicDto(1, "Epic1", "EpicDesc1", TaskStatus.NEW, List.of(
            new SubtaskDto(2, "ST1", "STD1", TaskStatus.NEW)
    ));
    private final EpicDto EPIC_UPDATE_SUBTASKS1_DONE = new EpicDto(1, "Epic1", "EpicDesc1", TaskStatus.DONE, List.of(
            new SubtaskDto(2, "UST1", "USTD1", TaskStatus.DONE)
    ));

    private final EpicDto EPIC_UPDATE_SUBTASKS_1_2_IN_PG = new EpicDto(1, "Epic1", "EpicDesc1", TaskStatus.IN_PROGRESS, List.of(
            new SubtaskDto(2, "UST1", "USTD1", TaskStatus.DONE),
            new SubtaskDto(3, "ST2", "STD2", TaskStatus.NEW)
    ));
    private final EpicDto EPIC_SUBTASKS_1DONE_2DONE = new EpicDto(1, "Epic1", "EpicDesc1", TaskStatus.DONE, List.of(
            new SubtaskDto(2, "UST1", "USTD1", TaskStatus.DONE),
            new SubtaskDto(3, "UST2", "USTD2", TaskStatus.DONE)
    ));
    private final EpicDto EPIC_UPDATE_SUBTASKS_1_2_NEW = new EpicDto(1, "Epic1", "EpicDesc1", TaskStatus.NEW, List.of(
            new SubtaskDto(2, "UST1", "USTD1", TaskStatus.NEW),
            new SubtaskDto(3, "UST2", "USTD2", TaskStatus.NEW)
    ));
    private final EpicDto EPIC_UPDATE_SUBTASKS_1NEW_2IN_PG = new EpicDto(1, "Epic1", "EpicDesc1", TaskStatus.IN_PROGRESS, List.of(
            new SubtaskDto(2, "UST1", "USTD1", TaskStatus.NEW),
            new SubtaskDto(3, "UST2", "USTD2", TaskStatus.IN_PROGRESS)
    ));
    private final EpicDto EPIC_SUBTASKS_1DONE_2DONE_3NEW = new EpicDto(1, "Epic1", "EpicDesc1", TaskStatus.IN_PROGRESS, List.of(
            new SubtaskDto(2, "UST1", "USTD1", TaskStatus.DONE),
            new SubtaskDto(3, "UST2", "USTD2", TaskStatus.DONE),
            new SubtaskDto(4, "ST3", "STD3", TaskStatus.NEW)
    ));

    private TaskManagerService taskManagerService() {
        return new TaskManagerService(
                new IdentifierGenerator(),
                new TaskRepositoryInMemory(),
                new MappingUtils()
        );
    }
}
