package test;

import java.util.List;
import main.model.TaskStatus;
import main.model.dto.create.CreateEpicDto;
import main.model.dto.create.CreateSubtaskDto;
import main.model.dto.create.CreateTaskDto;
import main.model.dto.response.EpicDto;
import main.model.dto.response.SubtaskDto;
import main.model.dto.response.TaskDto;
import main.model.dto.update.UpdateEpicDto;
import main.model.dto.update.UpdateSubtaskDto;
import main.model.dto.update.UpdateTaskDto;
import main.repository.TaskRepositoryInMemory;
import main.util.IdentifierGenerator;
import main.util.MappingUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.service.TaskManagerService;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerServiceTest {

    TaskManagerService taskManager;

    @Test
    void getNextTaskId() {
        assertEquals(IdentifierGenerator.INITIAL_IDENTIFIER, taskManager.getNextTaskId());
        taskManager.getNextTaskId();
        taskManager.getNextTaskId();
        taskManager.getNextTaskId();
        assertEquals(5, taskManager.getNextTaskId());
    }

    @BeforeEach
    void setUp() {
        taskManager = createTaskManagerService();
    }

    @Test
    void getAllEmpty() {
        assertTrue(taskManager.getAllEpics().isEmpty());
        assertTrue(taskManager.getAllSubtasks().isEmpty());
        assertTrue(taskManager.getAllTasks().isEmpty());
    }
    @Test
    void createEpic() {
        taskManager.createEpic(CREATE_EPIC1);

        assertEquals(
                List.of(
                        EPIC1_EMPTY
                ),
                taskManager.getAllEpics()
        );
    }

    @Test
    void getEpic() {
        EpicDto epic1 = taskManager.createEpic(CREATE_EPIC1);
        taskManager.createSubtask(CREATE_SUBTASK2, epic1.getId());

        assertEquals(
                EPIC1_ST2NEW,
                taskManager.getEpic(epic1.getId())
        );
    }

    @Test
    void removeEpic() {
        EpicDto epic1 = taskManager.createEpic(CREATE_EPIC1);
        taskManager.createSubtask(CREATE_SUBTASK2, epic1.getId());

        EpicDto epic2 = taskManager.createEpic(CREATE_EPIC5);
        taskManager.createSubtask(CREATE_SUBTASK4, epic2.getId());

        assertEquals(
                List.of(
                        EPIC1_ST2NEW,
                        EPIC5_ST4NEW
                ),
                taskManager.getAllEpics()
        );

        taskManager.removeEpic(epic1.getId());

        assertEquals(
                List.of(
                        EPIC5_ST4NEW
                ),
                taskManager.getAllEpics()
        );

        assertEquals(
                List.of(
                        SUBTASK4NEW
                ),
                taskManager.getAllSubtasks()
        );
    }

    @Test
    void updateEpic() {
        EpicDto epicDto = taskManager.createEpic(CREATE_EPIC1);
        taskManager.createSubtask(CREATE_SUBTASK2, epicDto.getId());
        taskManager.updateEpic(UPDATE_EPIC1_NEW_NAME);

        assertEquals(
                List.of(
                        EPIC1_NEW_NAME_ST1NEW
                ),
                taskManager.getAllEpics()
        );
    }

    @Test
    void removeAllEpics() {
        EpicDto epicDto = taskManager.createEpic(CREATE_EPIC1);
        taskManager.createSubtask(CREATE_SUBTASK2, epicDto.getId());
        taskManager.createSubtask(CREATE_SUBTASK3, epicDto.getId());

        EpicDto epic2 = taskManager.createEpic(CREATE_EPIC5);
        taskManager.createSubtask(CREATE_SUBTASK4, epic2.getId());

        assertEquals(
                List.of(
                        EPIC1_ST2NEW_ST3NEW,
                        EPIC5_ST4NEW
                ),
                taskManager.getAllEpics()
        );

        taskManager.removeAllEpics();

        assertEquals(
                List.of(),
                taskManager.getAllEpics()
        );

        assertEquals(
                List.of(),
                taskManager.getAllSubtasks()
        );
    }

    @Test
    void createSubtask() {

        EpicDto epicDto = taskManager.createEpic(CREATE_EPIC1);
        taskManager.createSubtask(CREATE_SUBTASK2, epicDto.getId());

        assertEquals(
                List.of(
                        EPIC1_ST2NEW
                ),
                taskManager.getAllEpics()
        );
    }

    @Test
    void getSubtask() {

        EpicDto epicDto = taskManager.createEpic(CREATE_EPIC1);
        SubtaskDto subtaskDto = taskManager.createSubtask(CREATE_SUBTASK2, epicDto.getId());

        assertEquals(
                SUBTASK2NEW,
                taskManager.getSubtask(subtaskDto.getId())
        );
    }

    @Test
    void updateSubtask() {

        EpicDto epicDto = taskManager.createEpic(CREATE_EPIC1);
        taskManager.createSubtask(CREATE_SUBTASK2, epicDto.getId());

        taskManager.updateSubtask(UPDATE_SUBTASK2_DONE);

        assertEquals(
                List.of(
                        EPIC1_ST2DONE
                ),
                taskManager.getAllEpics()
        );

        taskManager.createSubtask(CREATE_SUBTASK3, epicDto.getId());

        assertEquals(
                List.of(
                        EPIC1_ST2DONE_ST3NEW
                ),
                taskManager.getAllEpics()
        );

        taskManager.updateSubtask(UPDATE_SUBTASK3_DONE);

        assertEquals(
                List.of(
                        EPIC1_ST2DONE_ST3DONE
                ),
                taskManager.getAllEpics()
        );

        taskManager.updateSubtask(UPDATE_SUBTASK2_NEW);
        taskManager.updateSubtask(UPDATE_SUBTASK3_NEW);

        assertEquals(
                List.of(
                        EPIC1_ST2NEW_ST3NEW
                ),
                taskManager.getAllEpics()
        );

        taskManager.updateSubtask(UPDATE_SUBTASK2_NEW);
        taskManager.updateSubtask(UPDATE_SUBTASK3_IN_PG);

        assertEquals(
                List.of(
                        EPIC1_ST2NEW_ST3IN_PG
                ),
                taskManager.getAllEpics()
        );
    }

    @Test
    void removeSubtask() {
        EpicDto epicDto = taskManager.createEpic(CREATE_EPIC1);
        taskManager.createSubtask(CREATE_SUBTASK2, epicDto.getId());

        taskManager.createSubtask(CREATE_SUBTASK3, epicDto.getId());
        SubtaskDto subtaskDto = taskManager.createSubtask(CREATE_SUBTASK4, epicDto.getId());

        taskManager.updateSubtask(UPDATE_SUBTASK2_DONE);
        taskManager.updateSubtask(UPDATE_SUBTASK3_DONE);

        assertEquals(
                List.of(
                        EPIC1_ST2DONE_ST3DONE_ST4NEW
                ),
                taskManager.getAllEpics()
        );

        taskManager.removeSubtask(subtaskDto.getId());

        assertEquals(
                List.of(
                        EPIC1_ST2DONE_ST3DONE
                ),
                taskManager.getAllEpics()
        );
    }

    @Test
    void removeAllSubtasks() {
        EpicDto epicDto = taskManager.createEpic(CREATE_EPIC1);
        taskManager.createSubtask(CREATE_SUBTASK2, epicDto.getId());
        taskManager.createSubtask(CREATE_SUBTASK3, epicDto.getId());

        taskManager.updateSubtask(UPDATE_SUBTASK2_DONE);
        taskManager.updateSubtask(UPDATE_SUBTASK3_DONE);

        EpicDto epic2 = taskManager.createEpic(CREATE_EPIC5);
        taskManager.createSubtask(CREATE_SUBTASK4, epic2.getId());

        assertEquals(
                List.of(
                        EPIC1_ST2DONE_ST3DONE,
                        EPIC5_ST4NEW
                ),
                taskManager.getAllEpics()
        );

        taskManager.removeAllSubtasks();

        assertEquals(
                List.of(
                        EPIC1_EMPTY,
                        EPIC5_EMPTY
                ),
                taskManager.getAllEpics()
        );

        assertEquals(
                List.of(),
                taskManager.getAllSubtasks()
        );
    }

    @Test
    void createTask() {

        TaskDto task = taskManager.createTask(CREATE_TASK);
        assertEquals(
                List.of(
                        TASK(task.getId(), TaskStatus.NEW)
                ),
                taskManager.getAllTasks()
        );
    }
    @Test
    void updateTask() {
        TaskDto task = taskManager.createTask(CREATE_TASK);
        assertEquals(
                List.of(
                        TASK(task.getId(), TaskStatus.NEW)
                ),
                taskManager.getAllTasks()
        );
        taskManager.updateTask(UPDATE_TASK(task.getId(), TaskStatus.DONE));

        assertEquals(
                TASK(task.getId(), TaskStatus.DONE),
                taskManager.getTask(task.getId())
        );
    }

    @Test
    void removeTask() {
        TaskDto task1 = taskManager.createTask(CREATE_TASK);
        TaskDto task2 = taskManager.createTask(CREATE_TASK);
        assertEquals(
                List.of(
                        TASK(task1.getId(), TaskStatus.NEW),
                        TASK(task2.getId(), TaskStatus.NEW)
                ),
                taskManager.getAllTasks()
        );
        taskManager.removeTask(task1.getId());

        assertEquals(
                List.of(
                        TASK(task2.getId(), TaskStatus.NEW)
                ),
                taskManager.getAllTasks()
        );
    }

    @Test
    void removeAllTask() {
        TaskDto task1 = taskManager.createTask(CREATE_TASK);
        TaskDto task2 = taskManager.createTask(CREATE_TASK);
        assertEquals(
                List.of(
                        TASK(task1.getId(), TaskStatus.NEW),
                        TASK(task2.getId(), TaskStatus.NEW)
                ),
                taskManager.getAllTasks()
        );
        taskManager.removeAllTasks();

        assertEquals(
                List.of(),
                taskManager.getAllTasks()
        );
    }

    private final CreateEpicDto CREATE_EPIC1 = new CreateEpicDto(1, "name", "desc");
    private final CreateEpicDto CREATE_EPIC5 = new CreateEpicDto(5, "name", "desc");
    private final UpdateEpicDto UPDATE_EPIC1_NEW_NAME = new UpdateEpicDto(1, "new_name", "desc");
    private final CreateSubtaskDto CREATE_SUBTASK2 = new CreateSubtaskDto(2, "name", "desc");
    private final CreateSubtaskDto CREATE_SUBTASK3 = new CreateSubtaskDto(3, "name", "desc");
    private final CreateSubtaskDto CREATE_SUBTASK4 = new CreateSubtaskDto(4, "name", "desc");
    private final UpdateSubtaskDto UPDATE_SUBTASK2_DONE = new UpdateSubtaskDto(2, "name", "desc", TaskStatus.DONE);
    private final UpdateSubtaskDto UPDATE_SUBTASK2_NEW = new UpdateSubtaskDto(2, "name", "desc", TaskStatus.NEW);
    private final UpdateSubtaskDto UPDATE_SUBTASK3_DONE = new UpdateSubtaskDto(3, "name", "desc", TaskStatus.DONE);
    private final UpdateSubtaskDto UPDATE_SUBTASK3_NEW = new UpdateSubtaskDto(3, "name", "desc", TaskStatus.NEW);
    private final UpdateSubtaskDto UPDATE_SUBTASK3_IN_PG = new UpdateSubtaskDto(3, "name", "desc", TaskStatus.IN_PROGRESS);

    private final SubtaskDto SUBTASK2NEW = new SubtaskDto(2, "name", "desc", TaskStatus.NEW);
    private final SubtaskDto SUBTASK4NEW = new SubtaskDto(4, "name", "desc", TaskStatus.NEW);

    private final EpicDto EPIC1_ST2NEW = new EpicDto(1, "name", "desc", TaskStatus.NEW, List.of(
            SUBTASK2NEW
    ));
    private final EpicDto EPIC1_ST1NEW = new EpicDto(1, "name", "desc", TaskStatus.NEW, List.of(
            new SubtaskDto(2, "name", "desc", TaskStatus.NEW)
    ));
    private final EpicDto EPIC1_NEW_NAME_ST1NEW = new EpicDto(1, "new_name", "desc", TaskStatus.NEW, List.of(
            new SubtaskDto(2, "name", "desc", TaskStatus.NEW)
    ));
    private final EpicDto EPIC1_EMPTY = new EpicDto(1, "name", "desc", TaskStatus.NEW, List.of());
    private final EpicDto EPIC5_EMPTY = new EpicDto(5, "name", "desc", TaskStatus.NEW, List.of());
    private final EpicDto EPIC5_ST4NEW = new EpicDto(5, "name", "desc", TaskStatus.NEW, List.of(
            new SubtaskDto(4, "name", "desc", TaskStatus.NEW)
    ));
    private final EpicDto EPIC1_ST2DONE = new EpicDto(1, "name", "desc", TaskStatus.DONE, List.of(
            new SubtaskDto(2, "name", "desc", TaskStatus.DONE)
    ));
    private final EpicDto EPIC1_ST2DONE_ST3NEW = new EpicDto(1, "name", "desc", TaskStatus.IN_PROGRESS, List.of(
            new SubtaskDto(2, "name", "desc", TaskStatus.DONE),
            new SubtaskDto(3, "name", "desc", TaskStatus.NEW)
    ));
    private final EpicDto EPIC1_ST2DONE_ST3DONE = new EpicDto(1, "name", "desc", TaskStatus.DONE, List.of(
            new SubtaskDto(2, "name", "desc", TaskStatus.DONE),
            new SubtaskDto(3, "name", "desc", TaskStatus.DONE)
    ));
    private final EpicDto EPIC1_ST2NEW_ST3NEW = new EpicDto(1, "name", "desc", TaskStatus.NEW, List.of(
            new SubtaskDto(2, "name", "desc", TaskStatus.NEW),
            new SubtaskDto(3, "name", "desc", TaskStatus.NEW)
    ));
    private final EpicDto EPIC1_ST2NEW_ST3IN_PG = new EpicDto(1, "name", "desc", TaskStatus.IN_PROGRESS, List.of(
            new SubtaskDto(2, "name", "desc", TaskStatus.NEW),
            new SubtaskDto(3, "name", "desc", TaskStatus.IN_PROGRESS)
    ));
    private final EpicDto EPIC1_ST2DONE_ST3DONE_ST4NEW = new EpicDto(1, "name", "desc", TaskStatus.IN_PROGRESS, List.of(
            new SubtaskDto(2, "name", "desc", TaskStatus.DONE),
            new SubtaskDto(3, "name", "desc", TaskStatus.DONE),
            new SubtaskDto(4, "name", "desc", TaskStatus.NEW)
    ));

    private final CreateTaskDto CREATE_TASK = new CreateTaskDto("name", "desc");
    private UpdateTaskDto UPDATE_TASK(int id, TaskStatus status) { return new UpdateTaskDto(id, "name", "desc", status); }
    private TaskDto TASK(int id, TaskStatus status) { return new TaskDto(id, "name", "desc", status); }

    private TaskManagerService createTaskManagerService() {
        return new TaskManagerService(
                new IdentifierGenerator(),
                new TaskRepositoryInMemory(),
                new MappingUtils()
        );
    }
}
