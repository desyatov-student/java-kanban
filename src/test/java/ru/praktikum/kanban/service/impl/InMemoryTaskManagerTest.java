package ru.praktikum.kanban.service.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.praktikum.kanban.dto.CreateEpicDto;
import ru.praktikum.kanban.dto.CreateSubtaskDto;
import ru.praktikum.kanban.dto.EpicDto;
import ru.praktikum.kanban.dto.SubtaskDto;
import ru.praktikum.kanban.dto.TaskDto;
import ru.praktikum.kanban.exception.TaskValidationException;
import ru.praktikum.kanban.model.Subtask;
import ru.praktikum.kanban.model.Task;
import ru.praktikum.kanban.model.TaskStatus;
import ru.praktikum.kanban.repository.impl.InMemoryTaskRepository;
import ru.praktikum.kanban.service.HistoryManager;
import ru.praktikum.kanban.service.TaskManager;
import ru.praktikum.kanban.service.mapper.EpicMapper;
import ru.praktikum.kanban.service.mapper.EpicMapperImpl;
import ru.praktikum.kanban.service.mapper.SubtaskMapper;
import ru.praktikum.kanban.service.mapper.SubtaskMapperImpl;
import ru.praktikum.kanban.service.mapper.TaskMapper;
import ru.praktikum.kanban.service.mapper.TaskMapperImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.praktikum.kanban.helper.TaskFactory.CREATE_EPIC;
import static ru.praktikum.kanban.helper.TaskFactory.CREATE_SUBTASK;
import static ru.praktikum.kanban.helper.TaskFactory.CREATE_TASK;
import static ru.praktikum.kanban.helper.TaskFactory.EPIC;
import static ru.praktikum.kanban.helper.TaskFactory.EPIC_DTO;
import static ru.praktikum.kanban.helper.TaskFactory.SUBTASK;
import static ru.praktikum.kanban.helper.TaskFactory.SUBTASK_DTO;
import static ru.praktikum.kanban.helper.TaskFactory.TASK;
import static ru.praktikum.kanban.helper.TaskFactory.TASK_DTO;
import static ru.praktikum.kanban.helper.TaskFactory.UPDATE_EPIC;
import static ru.praktikum.kanban.helper.TaskFactory.UPDATE_SUBTASK;
import static ru.praktikum.kanban.helper.TaskFactory.UPDATE_TASK;

@ExtendWith(MockitoExtension.class)
class InMemoryTaskManagerTest {

    TaskManager taskManager;
    InMemoryTaskRepository repository;
    HistoryManager historyManager;

    private final TaskMapper taskMapper = new TaskMapperImpl();
    private final EpicMapper epicMapper = new EpicMapperImpl();
    private final SubtaskMapper subtaskMapper = new SubtaskMapperImpl();

    @BeforeEach
    void setUp() {
        repository = Mockito.spy(InMemoryTaskRepository.class);
        historyManager = Mockito.spy(new HistoryManagerImpl(repository));
        taskManager = new InMemoryTaskManager(
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
        Mockito.verify(repository, Mockito.times(1)).saveEpic(EPIC(epic.getId()));
    }

    @Test
    void getEpic() throws TaskValidationException {
        EpicDto epic = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtaskDto = taskManager.createSubtask(epic.getId(), CREATE_SUBTASK);

        assertEquals(
                EPIC_DTO(epic.getId(), TaskStatus.NEW, List.of(SUBTASK_DTO(subtaskDto.getId(), TaskStatus.NEW))),
                taskManager.getEpic(epic.getId()).get()
        );
    }

    @Test
    void getEpicAndResultIsEmpty() {
        assertEquals(Optional.empty(), taskManager.getEpic(10));
    }

    @Test
    void removeEpic() throws TaskValidationException {
        EpicDto epic1 = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtask1 = taskManager.createSubtask(epic1.getId(), CREATE_SUBTASK);

        EpicDto epic2 = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtask2 = taskManager.createSubtask(epic2.getId(), CREATE_SUBTASK);

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
        taskManager.updateEpic(epic.getId(), UPDATE_EPIC(newName));

        assertEquals(
                List.of(
                        EPIC_DTO(epic.getId(), newName, null)
                ),
                taskManager.getAllEpics()
        );
        Mockito.verify(repository, Mockito.times(2)).saveEpic(EPIC(epic.getId()));
    }

    @Test
    void removeAllEpics() throws TaskValidationException {
        EpicDto epic1 = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtask1 = taskManager.createSubtask(epic1.getId(), CREATE_SUBTASK);
        SubtaskDto subtask2 = taskManager.createSubtask(epic1.getId(), CREATE_SUBTASK);

        EpicDto epic2 = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtask3 = taskManager.createSubtask(epic2.getId(), CREATE_SUBTASK);

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
        SubtaskDto subtask = taskManager.createSubtask(epic.getId(), CREATE_SUBTASK);

        assertEquals(
                List.of(
                        EPIC_DTO(epic.getId(), TaskStatus.NEW, List.of(SUBTASK_DTO(subtask.getId(), TaskStatus.NEW)))
                ),
                taskManager.getAllEpics()
        );
        Mockito.verify(repository, Mockito.times(1)).saveSubtask(SUBTASK(subtask.getId()));
        Mockito.verify(repository, Mockito.times(2)).saveEpic(EPIC(epic.getId()));
    }

    @Test
    void createSubtaskShouldThrowTaskValidationExceptionWhenCreateTimeIntersectsExistTask() throws TaskValidationException {
        // given
        LocalDateTime startTime = LocalDateTime.now();
        Duration duration = Duration.ofHours(1);
        EpicDto epic = taskManager.createEpic(CREATE_EPIC());
        taskManager.createSubtask(epic.getId(), CREATE_SUBTASK(startTime, duration));

        // when
        TaskValidationException exception = assertThrows(
                TaskValidationException.class,
                () -> taskManager.createSubtask(epic.getId(), CREATE_SUBTASK(startTime, duration))
        );

        // then
        assertEquals("Could not validate task. Please change the start time", exception.getMessage());
    }

    @Test
    void createSubtaskShouldThrowTaskValidationExceptionWhenEpicNotFound() throws TaskValidationException {
        // given
        Integer epicId = 1;
        CreateSubtaskDto createSubtaskDto = CREATE_SUBTASK;

        // when
        TaskValidationException exception = assertThrows(
                TaskValidationException.class,
                () -> taskManager.createSubtask(epicId, createSubtaskDto)
        );

        // then
        assertEquals("Epic not found: " + epicId, exception.getMessage());
    }

    @Test
    void updateSubtaskShouldThrowTaskValidationExceptionWhenCreateTimeIntersectsExistTask() throws TaskValidationException {
        // given
        LocalDateTime startTime1 = LocalDateTime.now();
        LocalDateTime startTime2 = startTime1.plusHours(2);
        Duration duration = Duration.ofHours(1);
        EpicDto epic = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtaskDto1 = taskManager.createSubtask(epic.getId(), CREATE_SUBTASK(startTime1, duration));
        taskManager.createSubtask(epic.getId(), CREATE_SUBTASK(startTime2, duration));

        // when
        TaskValidationException exception = assertThrows(
                TaskValidationException.class,
                () -> taskManager.updateSubtask(subtaskDto1.getId(), UPDATE_SUBTASK(startTime2, duration))
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
        taskManager.createSubtask(epic.getId(), CREATE_SUBTASK(startTime, duration));
        startTime = startTime.plusHours(2);
        taskManager.createSubtask(epic.getId(), CREATE_SUBTASK(startTime, duration));
        startTime = startTime.plusHours(2);
        taskManager.createSubtask(epic.getId(), CREATE_SUBTASK(startTime, duration));

        // then
        EpicDto updatedEpic = taskManager.getEpic(epic.getId()).get();
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

        taskManager.createSubtask(epic.getId(), CREATE_SUBTASK(startTime, duration));
        startTime = startTime.plusHours(2);
        taskManager.createSubtask(epic.getId(), CREATE_SUBTASK(startTime, duration));
        startTime = startTime.plusHours(2);
        SubtaskDto subtask3 = taskManager.createSubtask(epic.getId(), CREATE_SUBTASK(startTime, duration));

        // when
        taskManager.removeSubtask(subtask3.getId());

        // then
        EpicDto updatedEpic = taskManager.getEpic(epic.getId()).get();
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

        taskManager.createSubtask(epic.getId(), CREATE_SUBTASK(startTime, duration));
        startTime = startTime.plusHours(2);
        taskManager.createSubtask(epic.getId(), CREATE_SUBTASK(startTime, duration));
        startTime = startTime.plusHours(2);
        taskManager.createSubtask(epic.getId(), CREATE_SUBTASK(startTime, duration));

        // when
        taskManager.removeAllSubtasks();

        // then
        EpicDto updatedEpic = taskManager.getEpic(epic.getId()).get();
        assertNull(updatedEpic.getStartTime());
        assertNull(updatedEpic.getDuration());
        assertNull(updatedEpic.getEndTime());

    }

    @Test
    void getSubtask() throws TaskValidationException {

        EpicDto epic = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtask = taskManager.createSubtask(epic.getId(), CREATE_SUBTASK);

        assertEquals(
                SUBTASK_DTO(subtask.getId(), TaskStatus.NEW),
                taskManager.getSubtask(subtask.getId()).get()
        );
    }

    @Test
    void getSubtaskAndResultIsEmpty() {
        assertEquals(Optional.empty(), taskManager.getSubtask(10));
    }

    @Test
    void getSubtaskWithEpicId() throws TaskValidationException {

        EpicDto epic = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtask1 = taskManager.createSubtask(epic.getId(), CREATE_SUBTASK);
        SubtaskDto subtask2 = taskManager.createSubtask(epic.getId(), CREATE_SUBTASK);

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
        SubtaskDto subtask1 = taskManager.createSubtask(epic.getId(), CREATE_SUBTASK);

        taskManager.updateSubtask(subtask1.getId(), UPDATE_SUBTASK(TaskStatus.DONE));

        assertEquals(
                List.of(
                        EPIC_DTO(epic.getId(), TaskStatus.DONE, List.of(
                                SUBTASK_DTO(subtask1.getId(), TaskStatus.DONE)
                        ))
                ),
                taskManager.getAllEpics()
        );
        Mockito.verify(repository, Mockito.times(1)).saveSubtask(SUBTASK(subtask1.getId()));
        Mockito.verify(repository, Mockito.times(3)).saveEpic(EPIC(epic.getId()));

        SubtaskDto subtask2 = taskManager.createSubtask(epic.getId(), CREATE_SUBTASK);

        assertEquals(
                List.of(
                        EPIC_DTO(epic.getId(), TaskStatus.IN_PROGRESS, List.of(
                                SUBTASK_DTO(subtask1.getId(), TaskStatus.DONE),
                                SUBTASK_DTO(subtask2.getId(), TaskStatus.NEW)
                        ))
                ),
                taskManager.getAllEpics()
        );

        taskManager.updateSubtask(subtask2.getId(), UPDATE_SUBTASK(TaskStatus.DONE));

        assertEquals(
                List.of(
                        EPIC_DTO(epic.getId(), TaskStatus.DONE, List.of(
                                SUBTASK_DTO(subtask1.getId(), TaskStatus.DONE),
                                SUBTASK_DTO(subtask2.getId(), TaskStatus.DONE)
                        ))
                ),
                taskManager.getAllEpics()
        );

        taskManager.updateSubtask(subtask1.getId(), UPDATE_SUBTASK(TaskStatus.NEW));
        taskManager.updateSubtask(subtask2.getId(), UPDATE_SUBTASK(TaskStatus.NEW));

        assertEquals(
                List.of(
                        EPIC_DTO(epic.getId(), TaskStatus.NEW, List.of(
                                SUBTASK_DTO(subtask1.getId(), TaskStatus.NEW),
                                SUBTASK_DTO(subtask2.getId(), TaskStatus.NEW)
                        ))
                ),
                taskManager.getAllEpics()
        );

        taskManager.updateSubtask(subtask1.getId(), UPDATE_SUBTASK(TaskStatus.NEW));
        taskManager.updateSubtask(subtask2.getId(), UPDATE_SUBTASK(TaskStatus.IN_PROGRESS));

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
        SubtaskDto subtask1 = taskManager.createSubtask(epic.getId(), CREATE_SUBTASK);

        SubtaskDto subtask2 = taskManager.createSubtask(epic.getId(), CREATE_SUBTASK);
        SubtaskDto subtask3 = taskManager.createSubtask(epic.getId(), CREATE_SUBTASK);

        taskManager.updateSubtask(subtask1.getId(), UPDATE_SUBTASK(TaskStatus.DONE));
        taskManager.updateSubtask(subtask2.getId(), UPDATE_SUBTASK(TaskStatus.DONE));

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

        Mockito.verify(repository, Mockito.times(3)).saveSubtask(Mockito.any());
        Mockito.verify(repository, Mockito.times(7)).saveEpic(EPIC(epic.getId()));
    }

    @Test
    void removeAllSubtasks() throws TaskValidationException {
        EpicDto epic1 = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtask1 = taskManager.createSubtask(epic1.getId(), CREATE_SUBTASK);
        SubtaskDto subtask2 = taskManager.createSubtask(epic1.getId(), CREATE_SUBTASK);

        taskManager.updateSubtask(subtask1.getId(), UPDATE_SUBTASK(TaskStatus.DONE));
        taskManager.updateSubtask(subtask2.getId(), UPDATE_SUBTASK(TaskStatus.DONE));

        EpicDto epic2 = taskManager.createEpic(CREATE_EPIC());
        SubtaskDto subtask3 = taskManager.createSubtask(epic2.getId(), CREATE_SUBTASK);

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
        Mockito.verify(repository, Mockito.times(1)).saveTask(TASK(task.getId()));
    }

    @Test
    void createTaskShouldThrowTaskValidationExceptionWhenCreateTimeIntersectsExistTask() throws TaskValidationException {
        // given
        LocalDateTime startTime = LocalDateTime.now();
        Duration duration = Duration.ofHours(1);
        TaskDto taskDto = taskManager.createTask(CREATE_TASK(startTime, duration));

        // when
        TaskValidationException exception = assertThrows(
                TaskValidationException.class,
                () -> taskManager.createTask(CREATE_TASK(startTime, duration))
        );

        // then
        assertEquals("Could not validate task. Please change the start time", exception.getMessage());
        Mockito.verify(repository, Mockito.times(1)).saveTask(TASK(taskDto.getId()));
    }

    @Test
    void updateTaskShouldThrowTaskValidationExceptionWhenCreateTimeIntersectsExistTask() throws TaskValidationException {
        // given
        LocalDateTime startTime1 = LocalDateTime.now();
        LocalDateTime startTime2 = startTime1.plusHours(2);
        Duration duration = Duration.ofHours(1);
        TaskDto taskDto1 = taskManager.createTask(CREATE_TASK(startTime1, duration));
        taskManager.createTask(CREATE_TASK(startTime2, duration));

        // when
        TaskValidationException exception = assertThrows(
                TaskValidationException.class,
                () -> taskManager.updateTask(taskDto1.getId(), UPDATE_TASK(startTime2, duration))
        );

        // then
        assertEquals("Could not validate task. Please change the start time", exception.getMessage());
        Mockito.verify(repository, Mockito.times(1)).saveTask(TASK(taskDto1.getId()));
    }

    @Test
    void getTaskAndResultIsEmpty() {
        assertEquals(Optional.empty(), taskManager.getTask(10));
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
        taskManager.updateTask(task.getId(), UPDATE_TASK(TaskStatus.DONE));

        assertEquals(
                TASK_DTO(task.getId(), TaskStatus.DONE),
                taskManager.getTask(task.getId()).get()
        );

        Mockito.verify(repository, Mockito.times(2)).saveTask(TASK(task.getId()));
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
        SubtaskDto subtask = taskManager.createSubtask(epic.getId(), CREATE_SUBTASK);

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
        SubtaskDto subtask = taskManager.createSubtask(epic.getId(), CREATE_SUBTASK);

        subtask =taskManager.getSubtask(subtask.getId()).get();
        epic = taskManager.getEpic(epic.getId()).get();
        task = taskManager.getTask(task.getId()).get();

        Mockito.verify(historyManager).add(epicMapper.toEntity(epic.getId(), CREATE_EPIC()));
        Mockito.verify(historyManager).add(taskMapper.toEntity(task.getId(), CREATE_TASK));
        Mockito.verify(historyManager).add(subtaskMapper.toEntity(subtask.getId(), epic.getId(), CREATE_SUBTASK));
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

    @Test
    void getPrioritizedTasksShouldMapTasksToDtoWhenRepositoryHasTasks() {
        // given
        LocalDateTime startTime = LocalDateTime.now();
        Duration duration = Duration.ofHours(1);
        Task task = TASK(1, startTime, duration);
        Subtask subtask = SUBTASK(2, 2, startTime, duration);

        Mockito.when(repository.getPrioritizedTasks()).thenReturn(List.of(task, subtask));

        List<TaskDto> expectedTasks = List.of(
                taskMapper.toDto(task),
                subtaskMapper.toDto(subtask)
        );

        // when
        List<TaskDto> actualTasks = taskManager.getPrioritizedTasks();

        // then
        assertEquals(expectedTasks, actualTasks);
    }
}
