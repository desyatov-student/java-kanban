package ru.praktikum.kanban.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import ru.praktikum.kanban.dto.CreateEpicDto;
import ru.praktikum.kanban.dto.CreateSubtaskDto;
import ru.praktikum.kanban.dto.CreateTaskDto;
import ru.praktikum.kanban.dto.EpicDto;
import ru.praktikum.kanban.dto.SubtaskDto;
import ru.praktikum.kanban.dto.TaskDto;
import ru.praktikum.kanban.dto.UpdateEpicDto;
import ru.praktikum.kanban.dto.UpdateSubtaskDto;
import ru.praktikum.kanban.dto.UpdateTaskDto;
import ru.praktikum.kanban.exception.TaskValidationException;
import ru.praktikum.kanban.model.Epic;
import ru.praktikum.kanban.model.Subtask;
import ru.praktikum.kanban.model.Task;
import ru.praktikum.kanban.model.TaskStatus;
import ru.praktikum.kanban.repository.TaskManagerRepository;
import ru.praktikum.kanban.service.HistoryManager;
import ru.praktikum.kanban.service.TaskManager;
import ru.praktikum.kanban.service.mapper.EpicMapper;
import ru.praktikum.kanban.service.mapper.EpicMapperImpl;
import ru.praktikum.kanban.service.mapper.SubtaskMapper;
import ru.praktikum.kanban.service.mapper.SubtaskMapperImpl;
import ru.praktikum.kanban.service.mapper.TaskMapper;
import ru.praktikum.kanban.service.mapper.TaskMapperImpl;
import ru.praktikum.kanban.util.AbstractMapper;
import ru.praktikum.kanban.util.IdentifierGenerator;

import static ru.praktikum.kanban.util.IdentifierGenerator.INITIAL_IDENTIFIER;

public class InMemoryTaskManager implements TaskManager {
    private final IdentifierGenerator identifierGenerator;
    private final TaskManagerRepository repository;
    private final HistoryManager historyManager;
    private final TaskMapper taskMapper;
    private final EpicMapper epicMapper;
    private final SubtaskMapper subtaskMapper;
    private final AbstractMapper<Task, TaskDto> abstractMapper;
    private final TaskScheduleValidator taskScheduleValidator;

    public InMemoryTaskManager(
            TaskManagerRepository repository,
            HistoryManager historyManager
    ) {
        this.identifierGenerator = new IdentifierGenerator(repository.getLastId().orElse(INITIAL_IDENTIFIER));
        this.repository = repository;
        this.historyManager = historyManager;
        this.taskMapper = new TaskMapperImpl();
        this.epicMapper = new EpicMapperImpl();
        this.subtaskMapper = new SubtaskMapperImpl();
        this.abstractMapper = new AbstractMapper<>();
        this.taskScheduleValidator = new TaskScheduleValidator();

        taskScheduleValidator.resetSchedule(repository.getPrioritizedTasks());

        abstractMapper.put(Task.class, taskMapper::toDto);
        abstractMapper.put(Subtask.class, value -> subtaskMapper.toDto((Subtask) value));
        abstractMapper.put(Epic.class, value -> this.getEpicDtoWithEpicEntity((Epic) value));
    }

    private Integer getNextTaskId() {
        return identifierGenerator.getNextId();
    }

    // Task's methods

    @Override
    public List<TaskDto> getAllTasks() {
        return repository.getAllTasks().stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDto createTask(CreateTaskDto createTaskDto) throws TaskValidationException {
        Task task = taskMapper.toEntity(getNextTaskId(), createTaskDto);
        validateNewTask(task);
        repository.saveTask(task);
        return taskMapper.toDto(task);
    }

    @Override
    public Optional<TaskDto> updateTask(Integer id, UpdateTaskDto updateTaskDto) throws TaskValidationException {
        Task task = repository.getTask(id);
        if (task == null) {
            return Optional.empty();
        }
        validateUpdateForTask(task, updateTaskDto);
        taskMapper.updateEntityFromDto(updateTaskDto, task);
        repository.saveTask(task);
        return Optional.of(taskMapper.toDto(task));
    }

    @Override
    public Optional<TaskDto> getTask(Integer id) {
        Task task = repository.getTask(id);
        if (task == null) {
            return Optional.empty();
        }
        historyManager.add(task);
        return Optional.of(taskMapper.toDto(task));
    }

    @Override
    public void removeTask(Integer id) {
        removeTaskFromRepository(id);
    }

    @Override
    public void removeAllTasks() {
        for (Task task : repository.getAllTasks()) {
            removeTaskFromRepository(task.getId());
        }
    }

    // Epic's methods

    @Override
    public List<EpicDto> getAllEpics() {
        List<Epic> epics = repository.getAllEpics();
        return epics.stream()
                .map(this::getEpicDtoWithEpicEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<EpicDto> getEpic(Integer id) {
        Epic epic = repository.getEpic(id);
        if (epic == null) {
            return Optional.empty();
        }
        historyManager.add(epic);
        return Optional.of(getEpicDtoWithEpicEntity(epic));
    }

    @Override
    public EpicDto createEpic(CreateEpicDto createEpicDto) {
        Epic epic = epicMapper.toEntity(getNextTaskId(), createEpicDto);
        repository.saveEpic(epic);
        return getEpicDtoWithEpicEntity(epic);
    }

    @Override
    public Optional<EpicDto> updateEpic(Integer id, UpdateEpicDto updateEpicDto) {
        Epic epic = repository.getEpic(id);
        if (epic == null) {
            return Optional.empty();
        }
        epicMapper.updateEntityFromDto(updateEpicDto, epic);
        repository.saveEpic(epic);
        return Optional.of(getEpicDtoWithEpicEntity(epic));
    }

    @Override
    public void removeEpic(Integer id) {
        Epic epic = repository.getEpic(id);
        if (epic == null) {
            return;
        }
        for (Integer subtaskId : epic.subtasks) {
            removeSubtaskFromRepository(subtaskId);
        }
        removeEpicFromRepository(epic.getId());
    }

    @Override
    public void removeAllEpics() {
        for (Epic epic : repository.getAllEpics()) {
            removeEpicFromRepository(epic.getId());
        }
        removeAllSubtaskFromRepository();
    }

    // Subtask's methods

    @Override
    public List<SubtaskDto> getAllSubtasks() {
        return repository.getAllSubtasks().stream()
                .map(subtaskMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<SubtaskDto> getSubtask(Integer id) {
        Subtask subtask = repository.getSubtask(id);
        if (subtask == null) {
            return Optional.empty();
        }
        historyManager.add(subtask);
        return Optional.of(subtaskMapper.toDto(subtask));
    }

    @Override
    public List<SubtaskDto> getSubtasksWithEpicId(Integer id) {
        Epic epic = repository.getEpic(id);
        if (epic == null) {
            return new ArrayList<>();
        }
        return epic.subtasks.stream()
                .map(repository::getSubtask)
                .map(subtaskMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public SubtaskDto createSubtask(CreateSubtaskDto createSubtaskDto) throws TaskValidationException {
        Integer epicId = createSubtaskDto.getEpicId();
        Epic epic = repository.getEpic(epicId);
        if (epic == null) {
            throw new TaskValidationException("Epic not found: " + epicId);
        }
        Subtask subtask = subtaskMapper.toEntity(getNextTaskId(), createSubtaskDto);
        validateNewTask(subtask);
        subtask.setEpicId(epicId);
        epic.subtasks.add(subtask.getId());

        repository.saveSubtask(subtask);
        updateEpicData(epic);
        return subtaskMapper.toDto(subtask);
    }

    @Override
    public SubtaskDto updateSubtask(Integer id, UpdateSubtaskDto updateSubtaskDto) throws TaskValidationException {
        Subtask subtask = repository.getSubtask(id);
        if (subtask == null) {
            return null;
        }
        validateUpdateForTask(subtask, updateSubtaskDto);
        Epic epic = repository.getEpic(subtask.getEpicId());
        if (epic == null) {
            return null;
        }
        subtaskMapper.updateEntityFromDto(updateSubtaskDto, subtask);
        subtask.setEpicId(epic.getId());
        updateEpicData(epic);
        return subtaskMapper.toDto(subtask);
    }

    @Override
    public void removeSubtask(Integer id) {
        Subtask subtask = repository.getSubtask(id);
        if (subtask == null) {
            return;
        }
        removeSubtaskFromRepository(id);
        Epic epic = repository.getEpic(subtask.getEpicId());
        if (epic == null) {
            return;
        }
        epic.subtasks.remove(id);
        updateEpicData(epic);
    }

    @Override
    public void removeAllSubtasks() {
        removeAllSubtaskFromRepository();
        for (Epic epic : repository.getAllEpics()) {
            epic.subtasks.clear();
            updateEpicData(epic);
        }
    }

    @Override
    public List<TaskDto> getHistory() {
        return historyManager.getHistory().stream()
                .map(abstractMapper::tryMap)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDto> getPrioritizedTasks() {
        return repository.getPrioritizedTasks().stream()
                .map(abstractMapper::tryMap)
                .collect(Collectors.toList());
    }

    private void validateUpdateForTask(Task task, UpdateTaskDto updateTaskDto) throws TaskValidationException {
        boolean isTimeChanged = isTimeChanged(task, updateTaskDto);
        if (isTimeChanged) {
            // Если у текущей таски есть время, то при обновлении это время удаляется из расписания,
            // чтобы освободить этот слот.
            if (!task.isTimeEmpty()) {
                taskScheduleValidator.resetScheduleForTaskTime(TaskTime.of(task));
            }
            // Если обновление содержит время, то мы его проверяем на пересечение с остальными и добавляем в расписание
            if (!updateTaskDto.isTimeEmpty()) {
                checkIntersection(TaskTime.of(updateTaskDto));
            }
        }
    }

    private void validateNewTask(Task task) throws TaskValidationException {
        if (task.isTimeEmpty()) {
            return;
        }
        checkIntersection(new TaskTime(task.getStartTime(), task.getEndTime()));
    }

    private void checkIntersection(TaskTime taskTime) throws TaskValidationException {
        boolean hasIntersection = taskScheduleValidator.checkIntersectionAndUpdateSchedule(taskTime);
        if (hasIntersection) {
            throw new TaskValidationException("Could not validate task. Please change the start time");
        }
    }

    private void removeTaskFromRepository(Integer taskId) {
        repository.removeTask(taskId);
        historyManager.remove(taskId);
    }

    private void removeEpicFromRepository(Integer epicId) {
        repository.removeEpic(epicId);
        historyManager.remove(epicId);
    }

    private void removeSubtaskFromRepository(Integer subtaskId) {
        repository.removeSubtask(subtaskId);
        historyManager.remove(subtaskId);
    }

    private void removeAllSubtaskFromRepository() {
        for (Subtask subtask : repository.getAllSubtasks()) {
            removeSubtaskFromRepository(subtask.getId());
        }
    }

    // Helpers

    private TaskStatus calculateEpicStatusWithSubtasks(List<Subtask> subtasks) {
        if (subtasks.isEmpty()) {
            return TaskStatus.NEW;
        }
        int size = subtasks.size();
        int newCount = 0;
        int doneCount = 0;
        for (Subtask subtask : subtasks) {
            switch (subtask.getStatus()) {
                case NEW:
                    newCount++;
                    break;
                case DONE:
                    doneCount++;
                    break;
                case IN_PROGRESS:
                default:
                    return TaskStatus.IN_PROGRESS;
            }
        }

        if (newCount == size) {
            return TaskStatus.NEW;
        } else if (doneCount == size) {
            return TaskStatus.DONE;
        }
        return TaskStatus.IN_PROGRESS;
    }

    private EpicTime calculateEpicTime(List<Subtask> subtasks) {
        if (subtasks.isEmpty()) {
            return new EpicTime();
        }
        return subtasks.stream()
                .filter(task -> !task.isTimeEmpty())
                .collect(new EpicTimeCollector());
    }

    private boolean isTimeChanged(Task task, UpdateTaskDto updateTaskDto) {
        if (task.isTimeEmpty() && updateTaskDto.isTimeEmpty()) {
            return false;
        }
        if (task.isTimeEmpty() || updateTaskDto.isTimeEmpty()) {
            return true;
        }
        return !updateTaskDto.getStartTime().isEqual(task.getStartTime())
                || !updateTaskDto.getEndTime().isEqual(task.getEndTime());
    }

    private void updateEpicData(Epic epic) {
        List<Subtask> subtasks = epic.subtasks.stream()
                .map(repository::getSubtask)
                .collect(Collectors.toList());

        epic.setStatus(calculateEpicStatusWithSubtasks(subtasks));
        EpicTime epicTime = calculateEpicTime(subtasks);
        epic.setStartTime(epicTime.getStartTime());
        epic.setDuration(epicTime.getDuration());
        epic.setEndTime(epicTime.getEndTime());
        repository.saveEpic(epic);
    }

    private EpicDto getEpicDtoWithEpicEntity(Epic epic) {
        List<SubtaskDto> subtasks = getSubtasksWithEpicId(epic.getId());
        return epicMapper.toDto(epic, subtasks);
    }
}
