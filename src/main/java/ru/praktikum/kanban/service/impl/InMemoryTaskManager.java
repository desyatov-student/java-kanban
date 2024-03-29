package ru.praktikum.kanban.service.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
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

public class InMemoryTaskManager implements TaskManager {
    private final IdentifierGenerator identifierGenerator;
    private final TaskManagerRepository repository;
    private final HistoryManager historyManager;
    private final TaskMapper taskMapper;
    private final EpicMapper epicMapper;
    private final SubtaskMapper subtaskMapper;
    private final AbstractMapper<Task, TaskDto> abstractMapper;
    private final TaskValidator taskValidator;

    public InMemoryTaskManager(
            IdentifierGenerator identifierGenerator,
            TaskManagerRepository repository,
            HistoryManager historyManager
    ) {
        this.identifierGenerator = identifierGenerator;
        this.repository = repository;
        this.historyManager = historyManager;
        this.taskMapper = new TaskMapperImpl();
        this.epicMapper = new EpicMapperImpl();
        this.subtaskMapper = new SubtaskMapperImpl();
        this.abstractMapper = new AbstractMapper<>();
        this.taskValidator = new TaskValidator();

        abstractMapper.put(Task.class, taskMapper::toDto);
        abstractMapper.put(Subtask.class, value -> subtaskMapper.toDto((Subtask) value));
        abstractMapper.put(Epic.class, value -> this.getEpicDtoWithEpicEntity((Epic) value));
    }

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    private static class TimeContainer {
        private LocalDateTime startTime;
        private Duration duration;
        private LocalDateTime endTime;
    }

    public InMemoryTaskManager(TaskManagerRepository repository, HistoryManager historyManager) {
        this(new IdentifierGenerator(), repository, historyManager);
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
        validateTask(task);
        repository.saveTask(task);
        return taskMapper.toDto(task);
    }

    @Override
    public TaskDto updateTask(UpdateTaskDto updateTaskDto) throws TaskValidationException {
        Task task = repository.getTask(updateTaskDto.getId());
        if (task == null) {
            return null;
        }
        validateTask(task);
        taskMapper.updateEntityFromDto(updateTaskDto, task);
        return taskMapper.toDto(task);
    }

    @Override
    public Optional<TaskDto> getTask(Integer taskId) {
        Task task = repository.getTask(taskId);
        if (task == null) {
            return Optional.empty();
        }
        historyManager.add(task);
        return Optional.of(taskMapper.toDto(task));
    }

    @Override
    public void removeTask(Integer taskId) {
        removeTaskFromRepository(taskId);
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
    public Optional<EpicDto> getEpic(Integer epicId) {
        Epic epic = repository.getEpic(epicId);
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
    public EpicDto updateEpic(UpdateEpicDto updateEpicDto) {
        Epic epic = repository.getEpic(updateEpicDto.getId());
        if (epic == null) {
            return null;
        }
        epicMapper.updateEntityFromDto(updateEpicDto, epic);
        return getEpicDtoWithEpicEntity(epic);
    }

    @Override
    public void removeEpic(Integer epicId) {
        Epic epic = repository.getEpic(epicId);
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
    public Optional<SubtaskDto> getSubtask(Integer subtaskId) {
        Subtask subtask = repository.getSubtask(subtaskId);
        if (subtask == null) {
            return Optional.empty();
        }
        historyManager.add(subtask);
        return Optional.of(subtaskMapper.toDto(subtask));
    }

    @Override
    public List<SubtaskDto> getSubtasksWithEpicId(Integer epicId) {
        Epic epic = repository.getEpic(epicId);
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
        validateTask(subtask);
        subtask.setEpicId(epicId);
        epic.subtasks.add(subtask.getId());

        repository.saveSubtask(subtask);
        updateEpicData(epic);

        return subtaskMapper.toDto(subtask);
    }

    @Override
    public SubtaskDto updateSubtask(UpdateSubtaskDto updateSubtaskDto) throws TaskValidationException {
        Subtask subtask = repository.getSubtask(updateSubtaskDto.getId());
        if (subtask == null) {
            return null;
        }
        validateTask(subtask);
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
    public void removeSubtask(Integer subtaskId) {
        Subtask subtask = repository.getSubtask(subtaskId);
        if (subtask == null) {
            return;
        }
        removeSubtaskFromRepository(subtaskId);
        Epic epic = repository.getEpic(subtask.getEpicId());
        if (epic == null) {
            return;
        }
        epic.subtasks.remove(subtaskId);
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

    private void validateTask(Task task) throws TaskValidationException {
        if (task.getStartTime() == null && task.getEndTime() == null) {
            return;
        }
        boolean hasIntersection = taskValidator.hasIntersectionOfTime(task, repository.getPrioritizedTasks());
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

    private TimeContainer calculateEpicTime(List<Subtask> subtasks) {
        if (subtasks.isEmpty()) {
            return new TimeContainer();
        }
        Predicate<Subtask> filter = (task) -> task != null && task.getStartTime() != null;
        return subtasks.stream().filter(filter)
                .reduce(new TimeContainer(), (accum, subtask) -> {
                    LocalDateTime startTime = minTime(accum.getStartTime(), subtask.getStartTime());
                    LocalDateTime endTime = maxTime(accum.getEndTime(), subtask.getEndTime());
                    Duration duration = sumNullableDuration(accum.duration, subtask.getDuration());
                    return new TimeContainer(startTime, duration, endTime);
                }, (accum1, accum2) -> accum2);
    }

    private Duration sumNullableDuration(Duration nullable, Duration duration) {
        return (nullable == null) ? duration : nullable.plus(duration);
    }

    private LocalDateTime minTime(LocalDateTime initial, LocalDateTime time) {
        if (initial == null) {
            return time;
        } else if (time.isBefore(initial)) {
            return time;
        }
        return initial;
    }

    private LocalDateTime maxTime(LocalDateTime initial, LocalDateTime time) {
        if (initial == null) {
            return time;
        } else if (time.isAfter(initial)) {
            return time;
        }
        return initial;
    }

    private void updateEpicData(Epic epic) {
        List<Subtask> subtasks = epic.subtasks.stream()
                .map(repository::getSubtask)
                .collect(Collectors.toList());

        epic.setStatus(calculateEpicStatusWithSubtasks(subtasks));
        TimeContainer timeContainer = calculateEpicTime(subtasks);
        epic.setStartTime(timeContainer.getStartTime());
        epic.setDuration(timeContainer.getDuration());
        epic.setEndTime(timeContainer.getEndTime());
    }

    private EpicDto getEpicDtoWithEpicEntity(Epic epic) {
        List<SubtaskDto> subtasks = getSubtasksWithEpicId(epic.getId());
        return epicMapper.toDto(epic, subtasks);
    }
}
