package ru.praktikum.kanban;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.praktikum.kanban.model.TaskStatus;
import ru.praktikum.kanban.model.dto.create.CreateEpicDto;
import ru.praktikum.kanban.model.dto.create.CreateSubtaskDto;
import ru.praktikum.kanban.model.dto.create.CreateTaskDto;
import ru.praktikum.kanban.model.dto.response.BaseTaskDto;
import ru.praktikum.kanban.model.dto.response.EpicDto;
import ru.praktikum.kanban.model.dto.response.SubtaskDto;
import ru.praktikum.kanban.model.dto.response.TaskDto;
import ru.praktikum.kanban.model.dto.update.UpdateEpicDto;
import ru.praktikum.kanban.model.dto.update.UpdateSubtaskDto;
import ru.praktikum.kanban.model.dto.update.UpdateTaskDto;
import ru.praktikum.kanban.model.mapper.EpicMapper;
import ru.praktikum.kanban.model.mapper.EpicMapperImpl;
import ru.praktikum.kanban.model.mapper.SubtaskMapper;
import ru.praktikum.kanban.model.mapper.SubtaskMapperImpl;
import ru.praktikum.kanban.model.mapper.TaskMapper;
import ru.praktikum.kanban.model.mapper.TaskMapperImpl;
import ru.praktikum.kanban.repository.impl.InMemoryTaskRepository;
import ru.praktikum.kanban.service.HistoryManager;
import ru.praktikum.kanban.service.TaskManager;
import ru.praktikum.kanban.service.impl.HistoryManagerImpl;
import ru.praktikum.kanban.service.impl.TaskManagerImpl;
import ru.praktikum.kanban.util.IdentifierGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskManagerImplTest {

    TaskManager taskManager;

    HistoryManager historyManager;

    private final TaskMapper taskMapper = new TaskMapperImpl();
    private final EpicMapper epicMapper = new EpicMapperImpl();
    private final SubtaskMapper subtaskMapper = new SubtaskMapperImpl();

    @BeforeEach
    void setUp() {
        InMemoryTaskRepository repository = new InMemoryTaskRepository();
        historyManager = Mockito.spy(new HistoryManagerImpl(repository));
        taskManager = new TaskManagerImpl(
                new IdentifierGenerator(),
                repository,
                historyManager
        );
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
        SubtaskDto subtaskDto = taskManager.createSubtask(CREATE_SUBTASK(epic.getId()));

        assertEquals(
                EPIC(epic.getId(), TaskStatus.NEW, List.of(SUBTASK(subtaskDto.getId(), TaskStatus.NEW))),
                taskManager.getEpic(epic.getId())
        );
    }

    @Test
    void getEpicAndResultIsEmpty() {
        assertNull(taskManager.getEpic(10));
    }

    @Test
    void removeEpic() {
        EpicDto epic1 = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtask1 = taskManager.createSubtask(CREATE_SUBTASK(epic1.getId()));

        EpicDto epic2 = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtask2 = taskManager.createSubtask(CREATE_SUBTASK(epic2.getId()));

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
        SubtaskDto subtask = taskManager.createSubtask(CREATE_SUBTASK(epic.getId()));
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
        SubtaskDto subtask1 = taskManager.createSubtask(CREATE_SUBTASK(epic1.getId()));
        SubtaskDto subtask2 = taskManager.createSubtask(CREATE_SUBTASK(epic1.getId()));

        EpicDto epic2 = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtask3 = taskManager.createSubtask(CREATE_SUBTASK(epic2.getId()));

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
        SubtaskDto subtask = taskManager.createSubtask(CREATE_SUBTASK(epic.getId()));

        assertEquals(
                List.of(
                        EPIC(epic.getId(), TaskStatus.NEW, List.of(SUBTASK(subtask.getId(), TaskStatus.NEW)))
                ),
                taskManager.getAllEpics()
        );
    }

    @Test
    void getSubtask() {

        EpicDto epic = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtask = taskManager.createSubtask(CREATE_SUBTASK(epic.getId()));

        assertEquals(
                SUBTASK(subtask.getId(), TaskStatus.NEW),
                taskManager.getSubtask(subtask.getId())
        );
    }

    @Test
    void getSubtaskAndResultIsEmpty() {
        assertNull(taskManager.getSubtask(10));
    }

    @Test
    void getSubtaskWithEpicId() {

        EpicDto epic = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtask1 = taskManager.createSubtask(CREATE_SUBTASK(epic.getId()));
        SubtaskDto subtask2 = taskManager.createSubtask(CREATE_SUBTASK(epic.getId()));

        assertEquals(
                List.of(
                        SUBTASK(subtask1.getId(), TaskStatus.NEW),
                        SUBTASK(subtask2.getId(), TaskStatus.NEW)
                ),
                taskManager.getSubtasksWithEpicId(epic.getId())
        );
    }

    @Test
    void getSubtaskWithEpicIdAndResultIsEmpty() {
        assertEquals(List.of(), taskManager.getSubtasksWithEpicId(10));
    }

    @Test
    void updateSubtask() {

        EpicDto epic = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtask1 = taskManager.createSubtask(CREATE_SUBTASK(epic.getId()));

        taskManager.updateSubtask(UPDATE_SUBTASK(subtask1.getId(), TaskStatus.DONE));

        assertEquals(
                List.of(
                        EPIC(epic.getId(), TaskStatus.DONE, List.of(
                                SUBTASK(subtask1.getId(), TaskStatus.DONE)
                        ))
                ),
                taskManager.getAllEpics()
        );

        SubtaskDto subtask2 = taskManager.createSubtask(CREATE_SUBTASK(epic.getId()));

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
        SubtaskDto subtask1 = taskManager.createSubtask(CREATE_SUBTASK(epic.getId()));

        SubtaskDto subtask2 = taskManager.createSubtask(CREATE_SUBTASK(epic.getId()));
        SubtaskDto subtask3 = taskManager.createSubtask(CREATE_SUBTASK(epic.getId()));

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
        SubtaskDto subtask1 = taskManager.createSubtask(CREATE_SUBTASK(epic1.getId()));
        SubtaskDto subtask2 = taskManager.createSubtask(CREATE_SUBTASK(epic1.getId()));

        taskManager.updateSubtask(UPDATE_SUBTASK(subtask1.getId(), TaskStatus.DONE));
        taskManager.updateSubtask(UPDATE_SUBTASK(subtask2.getId(), TaskStatus.DONE));

        EpicDto epic2 = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtask3 = taskManager.createSubtask(CREATE_SUBTASK(epic2.getId()));

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
    void getTaskAndResultIsEmpty() {
        assertNull(taskManager.getTask(10));
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

    @Test
    void getHistory() {
        EpicDto epic = taskManager.createEpic(CREATE_EPIC());
        TaskDto task = taskManager.createTask(CREATE_TASK);
        SubtaskDto subtask = taskManager.createSubtask(CREATE_SUBTASK(epic.getId()));

        taskManager.getSubtask(subtask.getId());
        taskManager.getEpic(epic.getId());
        taskManager.getTask(task.getId());
        taskManager.getSubtask(subtask.getId());

        final List<BaseTaskDto> history = taskManager.getHistory();
        assertEquals(
                List.of(
                        EPIC(epic.getId(), TaskStatus.NEW, List.of(subtask)),
                        task,
                        subtask
                ),
                history
        );
    }

    @Test
    void shouldRemoveTaskFromHistory() {
        EpicDto epic = taskManager.createEpic(CREATE_EPIC());
        TaskDto task = taskManager.createTask(CREATE_TASK);
        SubtaskDto subtask = taskManager.createSubtask(CREATE_SUBTASK(epic.getId()));

        subtask =taskManager.getSubtask(subtask.getId());
        epic = taskManager.getEpic(epic.getId());
        task = taskManager.getTask(task.getId());

        Mockito.verify(historyManager).add(epicMapper.toEntity(epic.getId(), CREATE_EPIC()));
        Mockito.verify(historyManager).add(taskMapper.toEntity(task.getId(), CREATE_TASK));
        Mockito.verify(historyManager).add(subtaskMapper.toEntity(subtask.getId(), CREATE_SUBTASK(epic.getId())));
        Mockito.verifyNoMoreInteractions(historyManager);

        taskManager.removeEpic(epic.getId());
        taskManager.removeTask(task.getId());

        Mockito.verify(historyManager).remove(epic.getId());
        Mockito.verify(historyManager).remove(subtask.getId());
        Mockito.verify(historyManager).remove(task.getId());
        Mockito.verifyNoMoreInteractions(historyManager);
    }

    @Test
    void shouldHistorySizeIs10() {

        int numbersOfTasks = 10;
        final ArrayList<BaseTaskDto> expected = new ArrayList<>();

        for (int i = 1; i <= numbersOfTasks; i++) {
            EpicDto epicDto = taskManager.createEpic(new CreateEpicDto("", ""));
            expected.add(epicDto);
        }

        for (int i = 1; i <= numbersOfTasks; i++) {
            taskManager.getEpic(i);
        }

        final List<BaseTaskDto> history = taskManager.getHistory();
        assertEquals(numbersOfTasks, history.size());
        assertEquals(expected, history);
    }

    private CreateEpicDto CREATE_EPIC() { return new CreateEpicDto("name", "desc"); }
    private UpdateEpicDto UPDATE_EPIC(int id, String name) { return new UpdateEpicDto(id, name, "desc"); }
    private CreateSubtaskDto CREATE_SUBTASK(int epicId) { return new CreateSubtaskDto("name", "desc", epicId); }
    private UpdateSubtaskDto UPDATE_SUBTASK(int id, TaskStatus status) { return new UpdateSubtaskDto(id, "name", "desc", status); }
    private SubtaskDto SUBTASK(int id, TaskStatus status) { return new SubtaskDto(id, "name", "desc", status); }
    private EpicDto EPIC(int id, TaskStatus status, List<SubtaskDto> subtasks) { return EPIC(id, "name", status, subtasks); }
    private EpicDto EPIC(int id, String name, TaskStatus status, List<SubtaskDto> subtasks) { return new EpicDto(id, name, "desc", status, subtasks); }

    private final CreateTaskDto CREATE_TASK = new CreateTaskDto("name", "desc");
    private UpdateTaskDto UPDATE_TASK(int id, TaskStatus status) { return new UpdateTaskDto(id, "name", "desc", status); }
    private TaskDto TASK(int id, TaskStatus status) { return new TaskDto(id, "name", "desc", status); }
}
