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
        EpicDto epic = taskManager.createEpic(CREATE_EPIC());

        assertEquals(
                List.of(
                        EPIC(epic.getId(), TaskStatus.NEW, List.of())
                ),
                taskManager.getAllEpics()
        );
    }

    @Test
    void getEpic() {
        EpicDto epic = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtaskDto = taskManager.createSubtask(CREATE_SUBTASK(), epic.getId());

        assertEquals(
                EPIC(epic.getId(), TaskStatus.NEW, List.of(SUBTASK(subtaskDto.getId(), TaskStatus.NEW))),
                taskManager.getEpic(epic.getId())
        );
    }

    @Test
    void removeEpic() {
        EpicDto epic1 = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtask1 = taskManager.createSubtask(CREATE_SUBTASK(), epic1.getId());

        EpicDto epic2 = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtask2 = taskManager.createSubtask(CREATE_SUBTASK(), epic2.getId());

        assertEquals(
                List.of(
                        EPIC(epic1.getId(), TaskStatus.NEW, List.of(SUBTASK(subtask1.getId(), TaskStatus.NEW))),
                        EPIC(epic2.getId(), TaskStatus.NEW, List.of(SUBTASK(subtask2.getId(), TaskStatus.NEW)))
                ),
                taskManager.getAllEpics()
        );

        taskManager.removeEpic(epic1.getId());

        assertEquals(
                List.of(
                        EPIC(epic2.getId(), TaskStatus.NEW, List.of(SUBTASK(subtask2.getId(), TaskStatus.NEW)))
                ),
                taskManager.getAllEpics()
        );

        assertEquals(
                List.of(
                        SUBTASK(subtask2.getId(), TaskStatus.NEW)
                ),
                taskManager.getAllSubtasks()
        );
    }

    @Test
    void updateEpic() {
        String newName = "new_name";
        EpicDto epic = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtask = taskManager.createSubtask(CREATE_SUBTASK(), epic.getId());
        taskManager.updateEpic(UPDATE_EPIC(epic.getId(), newName));

        assertEquals(
                List.of(
                        EPIC(epic.getId(), newName, TaskStatus.NEW, List.of(SUBTASK(subtask.getId(), TaskStatus.NEW)))
                ),
                taskManager.getAllEpics()
        );
    }

    @Test
    void removeAllEpics() {
        EpicDto epic1 = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtask1 = taskManager.createSubtask(CREATE_SUBTASK(), epic1.getId());
        SubtaskDto subtask2 = taskManager.createSubtask(CREATE_SUBTASK(), epic1.getId());

        EpicDto epic2 = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtask3 = taskManager.createSubtask(CREATE_SUBTASK(), epic2.getId());

        assertEquals(
                List.of(
                        EPIC(epic1.getId(), TaskStatus.NEW, List.of(
                                SUBTASK(subtask1.getId(), TaskStatus.NEW),
                                SUBTASK(subtask2.getId(), TaskStatus.NEW)
                        )),
                        EPIC(epic2.getId(), TaskStatus.NEW, List.of(
                                SUBTASK(subtask3.getId(), TaskStatus.NEW)
                        ))
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

        EpicDto epic = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtask = taskManager.createSubtask(CREATE_SUBTASK(), epic.getId());

        assertEquals(
                List.of(
                        EPIC(epic.getId(), TaskStatus.NEW, List.of(SUBTASK(subtask.getId(), TaskStatus.NEW)))
                ),
                taskManager.getAllEpics()
        );
    }

    @Test
    void getSubtask() {

        EpicDto epicDto = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtask = taskManager.createSubtask(CREATE_SUBTASK(), epicDto.getId());

        assertEquals(
                SUBTASK(subtask.getId(), TaskStatus.NEW),
                taskManager.getSubtask(subtask.getId())
        );
    }

    @Test
    void updateSubtask() {

        EpicDto epic = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtask1 = taskManager.createSubtask(CREATE_SUBTASK(), epic.getId());

        taskManager.updateSubtask(UPDATE_SUBTASK(subtask1.getId(), TaskStatus.DONE));

        assertEquals(
                List.of(
                        EPIC(epic.getId(), TaskStatus.DONE, List.of(
                                SUBTASK(subtask1.getId(), TaskStatus.DONE)
                        ))
                ),
                taskManager.getAllEpics()
        );

        SubtaskDto subtask2 = taskManager.createSubtask(CREATE_SUBTASK(), epic.getId());

        assertEquals(
                List.of(
                        EPIC(epic.getId(), TaskStatus.IN_PROGRESS, List.of(
                                SUBTASK(subtask1.getId(), TaskStatus.DONE),
                                SUBTASK(subtask2.getId(), TaskStatus.NEW)
                        ))
                ),
                taskManager.getAllEpics()
        );

        taskManager.updateSubtask(UPDATE_SUBTASK(subtask2.getId(), TaskStatus.DONE));

        assertEquals(
                List.of(
                        EPIC(epic.getId(), TaskStatus.DONE, List.of(
                                SUBTASK(subtask1.getId(), TaskStatus.DONE),
                                SUBTASK(subtask2.getId(), TaskStatus.DONE)
                        ))
                ),
                taskManager.getAllEpics()
        );

        taskManager.updateSubtask(UPDATE_SUBTASK(subtask1.getId(), TaskStatus.NEW));
        taskManager.updateSubtask(UPDATE_SUBTASK(subtask2.getId(), TaskStatus.NEW));

        assertEquals(
                List.of(
                        EPIC(epic.getId(), TaskStatus.NEW, List.of(
                                SUBTASK(subtask1.getId(), TaskStatus.NEW),
                                SUBTASK(subtask2.getId(), TaskStatus.NEW)
                        ))
                ),
                taskManager.getAllEpics()
        );

        taskManager.updateSubtask(UPDATE_SUBTASK(subtask1.getId(), TaskStatus.NEW));
        taskManager.updateSubtask(UPDATE_SUBTASK(subtask2.getId(), TaskStatus.IN_PROGRESS));

        assertEquals(
                List.of(
                        EPIC(epic.getId(), TaskStatus.IN_PROGRESS, List.of(
                                SUBTASK(subtask1.getId(), TaskStatus.NEW),
                                SUBTASK(subtask2.getId(), TaskStatus.IN_PROGRESS)
                        ))
                ),
                taskManager.getAllEpics()
        );
    }

    @Test
    void removeSubtask() {
        EpicDto epic = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtask1 = taskManager.createSubtask(CREATE_SUBTASK(), epic.getId());

        SubtaskDto subtask2 = taskManager.createSubtask(CREATE_SUBTASK(), epic.getId());
        SubtaskDto subtask3 = taskManager.createSubtask(CREATE_SUBTASK(), epic.getId());

        taskManager.updateSubtask(UPDATE_SUBTASK(subtask1.getId(), TaskStatus.DONE));
        taskManager.updateSubtask(UPDATE_SUBTASK(subtask2.getId(), TaskStatus.DONE));

        assertEquals(
                List.of(
                        EPIC(epic.getId(), TaskStatus.IN_PROGRESS, List.of(
                                SUBTASK(subtask1.getId(), TaskStatus.DONE),
                                SUBTASK(subtask2.getId(), TaskStatus.DONE),
                                SUBTASK(subtask3.getId(), TaskStatus.NEW)
                        ))
                ),
                taskManager.getAllEpics()
        );

        taskManager.removeSubtask(subtask3.getId());

        assertEquals(
                List.of(
                        EPIC(epic.getId(), TaskStatus.DONE, List.of(
                                SUBTASK(subtask1.getId(), TaskStatus.DONE),
                                SUBTASK(subtask2.getId(), TaskStatus.DONE)
                        ))
                ),
                taskManager.getAllEpics()
        );
    }

    @Test
    void removeAllSubtasks() {
        EpicDto epic1 = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtask1 = taskManager.createSubtask(CREATE_SUBTASK(), epic1.getId());
        SubtaskDto subtask2 = taskManager.createSubtask(CREATE_SUBTASK(), epic1.getId());

        taskManager.updateSubtask(UPDATE_SUBTASK(subtask1.getId(), TaskStatus.DONE));
        taskManager.updateSubtask(UPDATE_SUBTASK(subtask2.getId(), TaskStatus.DONE));

        EpicDto epic2 = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtask3 = taskManager.createSubtask(CREATE_SUBTASK(), epic2.getId());

        assertEquals(
                List.of(
                        EPIC(epic1.getId(), TaskStatus.DONE, List.of(
                                SUBTASK(subtask1.getId(), TaskStatus.DONE),
                                SUBTASK(subtask2.getId(), TaskStatus.DONE)
                        )),
                        EPIC(epic2.getId(), TaskStatus.NEW, List.of(
                                SUBTASK(subtask3.getId(), TaskStatus.NEW)
                        ))
                ),
                taskManager.getAllEpics()
        );

        taskManager.removeAllSubtasks();

        assertEquals(
                List.of(
                        EPIC(epic1.getId(), TaskStatus.NEW, List.of()),
                        EPIC(epic2.getId(), TaskStatus.NEW, List.of())
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

    private CreateEpicDto CREATE_EPIC() { return new CreateEpicDto("name", "desc"); }
    private UpdateEpicDto UPDATE_EPIC(int id, String name) { return new UpdateEpicDto(id, name, "desc"); }
    private CreateSubtaskDto CREATE_SUBTASK() { return new CreateSubtaskDto("name", "desc"); }
    private UpdateSubtaskDto UPDATE_SUBTASK(int id, TaskStatus status) { return new UpdateSubtaskDto(id, "name", "desc", status); }
    private SubtaskDto SUBTASK(int id, TaskStatus status) { return new SubtaskDto(id, "name", "desc", status); }
    private EpicDto EPIC(int id, TaskStatus status, List<SubtaskDto> subtasks) { return EPIC(id, "name", status, subtasks); }
    private EpicDto EPIC(int id, String name, TaskStatus status, List<SubtaskDto> subtasks) { return new EpicDto(id, name, "desc", status, subtasks); }

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
