package ru.praktikum.kanban.service.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.praktikum.kanban.dto.CreateTaskDto;
import ru.praktikum.kanban.dto.UpdateEpicDto;
import ru.praktikum.kanban.dto.UpdateSubtaskDto;
import ru.praktikum.kanban.exception.TaskValidationException;
import ru.praktikum.kanban.model.Subtask;
import ru.praktikum.kanban.model.TaskStatus;
import ru.praktikum.kanban.dto.CreateEpicDto;
import ru.praktikum.kanban.dto.CreateSubtaskDto;
import ru.praktikum.kanban.dto.EpicDto;
import ru.praktikum.kanban.dto.SubtaskDto;
import ru.praktikum.kanban.dto.TaskDto;
import ru.praktikum.kanban.dto.UpdateTaskDto;
import ru.praktikum.kanban.repository.impl.InMemoryTaskRepository;
import ru.praktikum.kanban.service.HistoryManager;
import ru.praktikum.kanban.service.TaskManager;
import ru.praktikum.kanban.service.mapper.EpicMapper;
import ru.praktikum.kanban.service.mapper.EpicMapperImpl;
import ru.praktikum.kanban.service.mapper.SubtaskMapper;
import ru.praktikum.kanban.service.mapper.SubtaskMapperImpl;
import ru.praktikum.kanban.service.mapper.TaskMapper;
import ru.praktikum.kanban.service.mapper.TaskMapperImpl;
import ru.praktikum.kanban.util.IdentifierGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.praktikum.kanban.helper.TaskFactory.DEFAULT_DESCRIPTION;
import static ru.praktikum.kanban.helper.TaskFactory.DEFAULT_NAME;
import static ru.praktikum.kanban.helper.TaskFactory.EPIC_DTO;
import static ru.praktikum.kanban.helper.TaskFactory.SUBTASK;
import static ru.praktikum.kanban.helper.TaskFactory.SUBTASK_DTO;
import static ru.praktikum.kanban.helper.TaskFactory.TASK_DTO;

class InMemoryTaskManagerTest {

    TaskManager taskManager;

    HistoryManager historyManager;

    private final TaskMapper taskMapper = new TaskMapperImpl();
    private final EpicMapper epicMapper = new EpicMapperImpl();
    private final SubtaskMapper subtaskMapper = new SubtaskMapperImpl();

    @BeforeEach
    void setUp() {
        InMemoryTaskRepository repository = new InMemoryTaskRepository();
        historyManager = Mockito.spy(new HistoryManagerImpl(repository));
        taskManager = new InMemoryTaskManager(
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
                        EPIC_DTO(epic.getId(), TaskStatus.NEW, List.of())
                ),
                taskManager.getAllEpics()
        );
    }

    @Test
    void getEpic() throws TaskValidationException {
        EpicDto epic = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtaskDto = taskManager.createSubtask(CREATE_SUBTASK(epic.getId()));

        assertEquals(
                EPIC_DTO(epic.getId(), TaskStatus.NEW, List.of(SUBTASK_DTO(subtaskDto.getId(), TaskStatus.NEW))),
                taskManager.getEpic(epic.getId())
        );
    }

    @Test
    void getEpicAndResultIsEmpty() {
        assertNull(taskManager.getEpic(10));
    }

    @Test
    void removeEpic() throws TaskValidationException {
        EpicDto epic1 = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtask1 = taskManager.createSubtask(CREATE_SUBTASK(epic1.getId()));

        EpicDto epic2 = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtask2 = taskManager.createSubtask(CREATE_SUBTASK(epic2.getId()));

        assertEquals(
                List.of(
                        EPIC_DTO(epic1.getId(), TaskStatus.NEW, List.of(SUBTASK_DTO(subtask1.getId(), TaskStatus.NEW))),
                        EPIC_DTO(epic2.getId(), TaskStatus.NEW, List.of(SUBTASK_DTO(subtask2.getId(), TaskStatus.NEW)))
                ),
                taskManager.getAllEpics()
        );

        taskManager.removeEpic(epic1.getId());

        assertEquals(
                List.of(
                        EPIC_DTO(epic2.getId(), TaskStatus.NEW, List.of(SUBTASK_DTO(subtask2.getId(), TaskStatus.NEW)))
                ),
                taskManager.getAllEpics()
        );

        assertEquals(
                List.of(
                        SUBTASK_DTO(subtask2.getId(), TaskStatus.NEW)
                ),
                taskManager.getAllSubtasks()
        );
    }

    @Test
    void updateEpic() {
        String newName = "new_name";
        EpicDto epic = taskManager.createEpic(CREATE_EPIC());
        taskManager.updateEpic(UPDATE_EPIC(epic.getId(), newName));

        assertEquals(
                List.of(
                        EPIC_DTO(epic.getId(), newName, null)
                ),
                taskManager.getAllEpics()
        );
    }

    @Test
    void removeAllEpics() throws TaskValidationException {
        EpicDto epic1 = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtask1 = taskManager.createSubtask(CREATE_SUBTASK(epic1.getId()));
        SubtaskDto subtask2 = taskManager.createSubtask(CREATE_SUBTASK(epic1.getId()));

        EpicDto epic2 = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtask3 = taskManager.createSubtask(CREATE_SUBTASK(epic2.getId()));

        assertEquals(
                List.of(
                        EPIC_DTO(epic1.getId(), TaskStatus.NEW, List.of(
                                SUBTASK_DTO(subtask1.getId(), TaskStatus.NEW),
                                SUBTASK_DTO(subtask2.getId(), TaskStatus.NEW)
                        )),
                        EPIC_DTO(epic2.getId(), TaskStatus.NEW, List.of(
                                SUBTASK_DTO(subtask3.getId(), TaskStatus.NEW)
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
    void createSubtask() throws TaskValidationException {

        EpicDto epic = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtask = taskManager.createSubtask(CREATE_SUBTASK(epic.getId()));

        assertEquals(
                List.of(
                        EPIC_DTO(epic.getId(), TaskStatus.NEW, List.of(SUBTASK_DTO(subtask.getId(), TaskStatus.NEW)))
                ),
                taskManager.getAllEpics()
        );
    }

    @Test
    void createSubtaskShouldThrowTaskValidationExceptionWhenCreateTimeIntersectsExistTask() throws TaskValidationException {
        // given
        LocalDateTime startTime = LocalDateTime.now();
        Duration duration = Duration.ofHours(1);
        EpicDto epic = taskManager.createEpic(CREATE_EPIC());
        taskManager.createSubtask(CREATE_SUBTASK(epic.getId(), startTime, duration));

        // when
        TaskValidationException exception = assertThrows(
                TaskValidationException.class,
                () -> taskManager.createSubtask(CREATE_SUBTASK(epic.getId(), startTime, duration))
        );

        // then
        assertEquals("Could not validate task. Please change the start time", exception.getMessage());
    }

    @Test
    void createSubtaskShouldThrowTaskValidationExceptionWhenEpicNotFound() throws TaskValidationException {
        // given
        Integer epicId = 1;
        CreateSubtaskDto createSubtaskDto = CREATE_SUBTASK(epicId);

        // when
        TaskValidationException exception = assertThrows(
                TaskValidationException.class,
                () -> taskManager.createSubtask(createSubtaskDto)
        );

        // then
        assertEquals("Epic not found: " + epicId, exception.getMessage());
    }

    @Test
    void updateSubtaskShouldThrowTaskValidationExceptionWhenCreateTimeIntersectsExistTask() throws TaskValidationException {
        // given
        LocalDateTime startTime = LocalDateTime.now();
        Duration duration = Duration.ofHours(1);
        EpicDto epic = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtaskDto = taskManager.createSubtask(CREATE_SUBTASK(epic.getId(), startTime, duration));

        // when
        TaskValidationException exception = assertThrows(
                TaskValidationException.class,
                () -> taskManager.updateSubtask(UPDATE_SUBTASK(subtaskDto.getId(), startTime, duration))
        );

        // then
        assertEquals("Could not validate task. Please change the start time", exception.getMessage());
    }

    @Test
    void createSubtaskShouldCalculateEpicTimeWhenSaveSubtasks() throws TaskValidationException {
        // given
        EpicDto epic = taskManager.createEpic(CREATE_EPIC());
        LocalDateTime startTime = LocalDateTime.of(2000, 1,1, 0,0);
        Duration duration = Duration.ofHours(1);

        // when
        taskManager.createSubtask(CREATE_SUBTASK(epic.getId(), startTime, duration));
        startTime = startTime.plusHours(2);
        taskManager.createSubtask(CREATE_SUBTASK(epic.getId(), startTime, duration));
        startTime = startTime.plusHours(2);
        taskManager.createSubtask(CREATE_SUBTASK(epic.getId(), startTime, duration));

        // then
        EpicDto updatedEpic = taskManager.getEpic(epic.getId());
        assertEquals(
                LocalDateTime.of(2000, 1,1, 0,0),
                updatedEpic.getStartTime());
        assertEquals(
                Duration.ofHours(3),
                updatedEpic.getDuration()
        );
        assertEquals(
                LocalDateTime.of(2000, 1,1, 5,0),
                updatedEpic.getEndTime()
        );

    }

    @Test
    void removeSubtaskShouldReCalculateEpicTimeWhenRemove1Subtask() throws TaskValidationException {
        // given
        EpicDto epic = taskManager.createEpic(CREATE_EPIC());
        LocalDateTime startTime = LocalDateTime.of(2000, 1,1, 0,0);
        Duration duration = Duration.ofHours(1);

        taskManager.createSubtask(CREATE_SUBTASK(epic.getId(), startTime, duration));
        startTime = startTime.plusHours(2);
        taskManager.createSubtask(CREATE_SUBTASK(epic.getId(), startTime, duration));
        startTime = startTime.plusHours(2);
        SubtaskDto subtask3 = taskManager.createSubtask(CREATE_SUBTASK(epic.getId(), startTime, duration));

        // when
        taskManager.removeSubtask(subtask3.getId());

        // then
        EpicDto updatedEpic = taskManager.getEpic(epic.getId());
        assertEquals(
                LocalDateTime.of(2000, 1,1, 0,0),
                updatedEpic.getStartTime());
        assertEquals(
                Duration.ofHours(2),
                updatedEpic.getDuration()
        );
        assertEquals(
                LocalDateTime.of(2000, 1,1, 3,0),
                updatedEpic.getEndTime()
        );

    }

    @Test
    void removeAllSubtasksShouldEpicHasEmptyTimeWhenEpicDoesNotHaveSubtasks() throws TaskValidationException {
        // given
        EpicDto epic = taskManager.createEpic(CREATE_EPIC());
        LocalDateTime startTime = LocalDateTime.of(2000, 1,1, 0,0);
        Duration duration = Duration.ofHours(1);

        taskManager.createSubtask(CREATE_SUBTASK(epic.getId(), startTime, duration));
        startTime = startTime.plusHours(2);
        taskManager.createSubtask(CREATE_SUBTASK(epic.getId(), startTime, duration));
        startTime = startTime.plusHours(2);
        taskManager.createSubtask(CREATE_SUBTASK(epic.getId(), startTime, duration));

        // when
        taskManager.removeAllSubtasks();

        // then
        EpicDto updatedEpic = taskManager.getEpic(epic.getId());
        assertNull(updatedEpic.getStartTime());
        assertNull(updatedEpic.getDuration());
        assertNull(updatedEpic.getEndTime());

    }

    @Test
    void getSubtask() throws TaskValidationException {

        EpicDto epic = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtask = taskManager.createSubtask(CREATE_SUBTASK(epic.getId()));

        assertEquals(
                SUBTASK_DTO(subtask.getId(), TaskStatus.NEW),
                taskManager.getSubtask(subtask.getId())
        );
    }

    @Test
    void getSubtaskAndResultIsEmpty() {
        assertNull(taskManager.getSubtask(10));
    }

    @Test
    void getSubtaskWithEpicId() throws TaskValidationException {

        EpicDto epic = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtask1 = taskManager.createSubtask(CREATE_SUBTASK(epic.getId()));
        SubtaskDto subtask2 = taskManager.createSubtask(CREATE_SUBTASK(epic.getId()));

        assertEquals(
                List.of(
                        SUBTASK_DTO(subtask1.getId(), TaskStatus.NEW),
                        SUBTASK_DTO(subtask2.getId(), TaskStatus.NEW)
                ),
                taskManager.getSubtasksWithEpicId(epic.getId())
        );
    }

    @Test
    void getSubtaskWithEpicIdAndResultIsEmpty() {
        assertEquals(List.of(), taskManager.getSubtasksWithEpicId(10));
    }

    @Test
    void updateSubtask() throws TaskValidationException {

        EpicDto epic = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtask1 = taskManager.createSubtask(CREATE_SUBTASK(epic.getId()));

        taskManager.updateSubtask(UPDATE_SUBTASK(subtask1.getId(), TaskStatus.DONE));

        assertEquals(
                List.of(
                        EPIC_DTO(epic.getId(), TaskStatus.DONE, List.of(
                                SUBTASK_DTO(subtask1.getId(), TaskStatus.DONE)
                        ))
                ),
                taskManager.getAllEpics()
        );

        SubtaskDto subtask2 = taskManager.createSubtask(CREATE_SUBTASK(epic.getId()));

        assertEquals(
                List.of(
                        EPIC_DTO(epic.getId(), TaskStatus.IN_PROGRESS, List.of(
                                SUBTASK_DTO(subtask1.getId(), TaskStatus.DONE),
                                SUBTASK_DTO(subtask2.getId(), TaskStatus.NEW)
                        ))
                ),
                taskManager.getAllEpics()
        );

        taskManager.updateSubtask(UPDATE_SUBTASK(subtask2.getId(), TaskStatus.DONE));

        assertEquals(
                List.of(
                        EPIC_DTO(epic.getId(), TaskStatus.DONE, List.of(
                                SUBTASK_DTO(subtask1.getId(), TaskStatus.DONE),
                                SUBTASK_DTO(subtask2.getId(), TaskStatus.DONE)
                        ))
                ),
                taskManager.getAllEpics()
        );

        taskManager.updateSubtask(UPDATE_SUBTASK(subtask1.getId(), TaskStatus.NEW));
        taskManager.updateSubtask(UPDATE_SUBTASK(subtask2.getId(), TaskStatus.NEW));

        assertEquals(
                List.of(
                        EPIC_DTO(epic.getId(), TaskStatus.NEW, List.of(
                                SUBTASK_DTO(subtask1.getId(), TaskStatus.NEW),
                                SUBTASK_DTO(subtask2.getId(), TaskStatus.NEW)
                        ))
                ),
                taskManager.getAllEpics()
        );

        taskManager.updateSubtask(UPDATE_SUBTASK(subtask1.getId(), TaskStatus.NEW));
        taskManager.updateSubtask(UPDATE_SUBTASK(subtask2.getId(), TaskStatus.IN_PROGRESS));

        assertEquals(
                List.of(
                        EPIC_DTO(epic.getId(), TaskStatus.IN_PROGRESS, List.of(
                                SUBTASK_DTO(subtask1.getId(), TaskStatus.NEW),
                                SUBTASK_DTO(subtask2.getId(), TaskStatus.IN_PROGRESS)
                        ))
                ),
                taskManager.getAllEpics()
        );
    }

    @Test
    void removeSubtask() throws TaskValidationException {
        EpicDto epic = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtask1 = taskManager.createSubtask(CREATE_SUBTASK(epic.getId()));

        SubtaskDto subtask2 = taskManager.createSubtask(CREATE_SUBTASK(epic.getId()));
        SubtaskDto subtask3 = taskManager.createSubtask(CREATE_SUBTASK(epic.getId()));

        taskManager.updateSubtask(UPDATE_SUBTASK(subtask1.getId(), TaskStatus.DONE));
        taskManager.updateSubtask(UPDATE_SUBTASK(subtask2.getId(), TaskStatus.DONE));

        assertEquals(
                List.of(
                        EPIC_DTO(epic.getId(), TaskStatus.IN_PROGRESS, List.of(
                                SUBTASK_DTO(subtask1.getId(), TaskStatus.DONE),
                                SUBTASK_DTO(subtask2.getId(), TaskStatus.DONE),
                                SUBTASK_DTO(subtask3.getId(), TaskStatus.NEW)
                        ))
                ),
                taskManager.getAllEpics()
        );

        taskManager.removeSubtask(subtask3.getId());

        assertEquals(
                List.of(
                        EPIC_DTO(epic.getId(), TaskStatus.DONE, List.of(
                                SUBTASK_DTO(subtask1.getId(), TaskStatus.DONE),
                                SUBTASK_DTO(subtask2.getId(), TaskStatus.DONE)
                        ))
                ),
                taskManager.getAllEpics()
        );
    }

    @Test
    void removeAllSubtasks() throws TaskValidationException {
        EpicDto epic1 = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtask1 = taskManager.createSubtask(CREATE_SUBTASK(epic1.getId()));
        SubtaskDto subtask2 = taskManager.createSubtask(CREATE_SUBTASK(epic1.getId()));

        taskManager.updateSubtask(UPDATE_SUBTASK(subtask1.getId(), TaskStatus.DONE));
        taskManager.updateSubtask(UPDATE_SUBTASK(subtask2.getId(), TaskStatus.DONE));

        EpicDto epic2 = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtask3 = taskManager.createSubtask(CREATE_SUBTASK(epic2.getId()));

        assertEquals(
                List.of(
                        EPIC_DTO(epic1.getId(), TaskStatus.DONE, List.of(
                                SUBTASK_DTO(subtask1.getId(), TaskStatus.DONE),
                                SUBTASK_DTO(subtask2.getId(), TaskStatus.DONE)
                        )),
                        EPIC_DTO(epic2.getId(), TaskStatus.NEW, List.of(
                                SUBTASK_DTO(subtask3.getId(), TaskStatus.NEW)
                        ))
                ),
                taskManager.getAllEpics()
        );

        taskManager.removeAllSubtasks();

        assertEquals(
                List.of(
                        EPIC_DTO(epic1.getId(), TaskStatus.NEW, List.of()),
                        EPIC_DTO(epic2.getId(), TaskStatus.NEW, List.of())
                ),
                taskManager.getAllEpics()
        );

        assertEquals(
                List.of(),
                taskManager.getAllSubtasks()
        );
    }

    @Test
    void createTask() throws TaskValidationException {

        TaskDto task = taskManager.createTask(CREATE_TASK);
        assertEquals(
                List.of(
                        TASK_DTO(task.getId(), TaskStatus.NEW)
                ),
                taskManager.getAllTasks()
        );
    }

    @Test
    void createTaskShouldThrowTaskValidationExceptionWhenCreateTimeIntersectsExistTask() throws TaskValidationException {
        // given
        LocalDateTime startTime = LocalDateTime.now();
        Duration duration = Duration.ofHours(1);
        taskManager.createTask(CREATE_TASK(startTime, duration));

        // when
        TaskValidationException exception = assertThrows(
                TaskValidationException.class,
                () -> taskManager.createTask(CREATE_TASK(startTime, duration))
        );

        // then
        assertEquals("Could not validate task. Please change the start time", exception.getMessage());
    }

    @Test
    void updateTaskShouldThrowTaskValidationExceptionWhenCreateTimeIntersectsExistTask() throws TaskValidationException {
        // given
        LocalDateTime startTime = LocalDateTime.now();
        Duration duration = Duration.ofHours(1);
        TaskDto taskDto = taskManager.createTask(CREATE_TASK(startTime, duration));

        // when
        TaskValidationException exception = assertThrows(
                TaskValidationException.class,
                () -> taskManager.updateTask(UPDATE_TASK(taskDto.getId(), startTime, duration))
        );

        // then
        assertEquals("Could not validate task. Please change the start time", exception.getMessage());
    }

    @Test
    void getTaskAndResultIsEmpty() {
        assertNull(taskManager.getTask(10));
    }

    @Test
    void updateTask() throws TaskValidationException {
        TaskDto task = taskManager.createTask(CREATE_TASK);
        assertEquals(
                List.of(
                        TASK_DTO(task.getId(), TaskStatus.NEW)
                ),
                taskManager.getAllTasks()
        );
        taskManager.updateTask(UPDATE_TASK(task.getId(), TaskStatus.DONE));

        assertEquals(
                TASK_DTO(task.getId(), TaskStatus.DONE),
                taskManager.getTask(task.getId())
        );
    }

    @Test
    void removeTask() throws TaskValidationException {
        TaskDto task1 = taskManager.createTask(CREATE_TASK);
        TaskDto task2 = taskManager.createTask(CREATE_TASK);
        assertEquals(
                List.of(
                        TASK_DTO(task1.getId(), TaskStatus.NEW),
                        TASK_DTO(task2.getId(), TaskStatus.NEW)
                ),
                taskManager.getAllTasks()
        );
        taskManager.removeTask(task1.getId());

        assertEquals(
                List.of(
                        TASK_DTO(task2.getId(), TaskStatus.NEW)
                ),
                taskManager.getAllTasks()
        );
    }

    @Test
    void removeAllTask() throws TaskValidationException {
        TaskDto task1 = taskManager.createTask(CREATE_TASK);
        TaskDto task2 = taskManager.createTask(CREATE_TASK);
        assertEquals(
                List.of(
                        TASK_DTO(task1.getId(), TaskStatus.NEW),
                        TASK_DTO(task2.getId(), TaskStatus.NEW)
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
    void getHistory() throws TaskValidationException {
        EpicDto epic = taskManager.createEpic(CREATE_EPIC());
        TaskDto task = taskManager.createTask(CREATE_TASK);
        SubtaskDto subtask = taskManager.createSubtask(CREATE_SUBTASK(epic.getId()));

        taskManager.getSubtask(subtask.getId());
        taskManager.getEpic(epic.getId());
        taskManager.getTask(task.getId());
        taskManager.getSubtask(subtask.getId());

        final List<TaskDto> history = taskManager.getHistory();
        assertEquals(
                List.of(
                        EPIC_DTO(epic.getId(), TaskStatus.NEW, List.of(subtask)),
                        task,
                        subtask
                ),
                history
        );
    }

    @Test
    void shouldRemoveTaskFromHistory() throws TaskValidationException {
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
        final ArrayList<TaskDto> expected = new ArrayList<>();

        for (int i = 1; i <= numbersOfTasks; i++) {
            EpicDto epicDto = taskManager.createEpic(new CreateEpicDto("", ""));
            expected.add(epicDto);
        }

        for (int i = 1; i <= numbersOfTasks; i++) {
            taskManager.getEpic(i);
        }

        final List<TaskDto> history = taskManager.getHistory();
        assertEquals(numbersOfTasks, history.size());
        assertEquals(expected, history);
    }

    private CreateEpicDto CREATE_EPIC() { return new CreateEpicDto(DEFAULT_NAME, DEFAULT_DESCRIPTION); }
    private UpdateEpicDto UPDATE_EPIC(Integer id, String name) { return new UpdateEpicDto(id, name, DEFAULT_DESCRIPTION); }
    private CreateSubtaskDto CREATE_SUBTASK(Integer epicId) { return CREATE_SUBTASK(epicId, null, null); }
    private CreateSubtaskDto CREATE_SUBTASK(
            Integer epicId,
            LocalDateTime startTime,
            Duration duration
    ) { return new CreateSubtaskDto(DEFAULT_NAME, DEFAULT_DESCRIPTION, epicId, startTime, duration); }
    private UpdateSubtaskDto UPDATE_SUBTASK(Integer id, TaskStatus status) { return new UpdateSubtaskDto(id, DEFAULT_NAME, DEFAULT_DESCRIPTION, status, null, null); }
    private UpdateSubtaskDto UPDATE_SUBTASK(
            Integer id,
            LocalDateTime startTime,
            Duration duration
    ) { return new UpdateSubtaskDto(id, DEFAULT_NAME, DEFAULT_DESCRIPTION, TaskStatus.NEW, startTime, duration); }
    private final CreateTaskDto CREATE_TASK = CREATE_TASK(null, null);
    private CreateTaskDto CREATE_TASK(
            LocalDateTime startTime,
            Duration duration
    ) { return new CreateTaskDto(DEFAULT_NAME, DEFAULT_DESCRIPTION, startTime, duration); }
    private UpdateTaskDto UPDATE_TASK(Integer id, TaskStatus status) { return new UpdateTaskDto(id, DEFAULT_NAME, DEFAULT_DESCRIPTION, status); }
    private UpdateTaskDto UPDATE_TASK(
            Integer id,
            LocalDateTime startTime,
            Duration duration
    ) { return new UpdateTaskDto(id, DEFAULT_NAME, DEFAULT_DESCRIPTION, TaskStatus.NEW, startTime, duration); }
}
