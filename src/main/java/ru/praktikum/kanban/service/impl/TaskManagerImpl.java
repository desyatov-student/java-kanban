package ru.praktikum.kanban.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import ru.praktikum.kanban.model.TaskStatus;
import ru.praktikum.kanban.model.dto.create.CreateEpicDto;
import ru.praktikum.kanban.model.dto.create.CreateSubtaskDto;
import ru.praktikum.kanban.model.dto.create.CreateTaskDto;
import ru.praktikum.kanban.model.dto.response.EpicDto;
import ru.praktikum.kanban.model.dto.response.SubtaskDto;
import ru.praktikum.kanban.model.dto.response.TaskDto;
import ru.praktikum.kanban.model.dto.update.UpdateEpic;
import ru.praktikum.kanban.model.dto.update.UpdateSubtask;
import ru.praktikum.kanban.model.dto.update.UpdateTask;
import ru.praktikum.kanban.model.entity.Epic;
import ru.praktikum.kanban.model.entity.Subtask;
import ru.praktikum.kanban.model.entity.Task;
import ru.praktikum.kanban.service.mapper.EpicMapperImpl;
import ru.praktikum.kanban.service.mapper.SubtaskMapperImpl;
import ru.praktikum.kanban.service.mapper.TaskMapperImpl;
import ru.praktikum.kanban.repository.TaskManagerRepository;
import ru.praktikum.kanban.service.HistoryManager;
import ru.praktikum.kanban.service.TaskManager;
import ru.praktikum.kanban.service.mapper.EpicMapper;
import ru.praktikum.kanban.service.mapper.SubtaskMapper;
import ru.praktikum.kanban.service.mapper.TaskMapper;
import ru.praktikum.kanban.util.AbstractMapper;
import ru.praktikum.kanban.util.IdentifierGenerator;

public class TaskManagerImpl implements TaskManager {
    private final IdentifierGenerator identifierGenerator;
    private final TaskManagerRepository repository;
    private final HistoryManager historyManager;
    private final TaskMapper taskMapper;
    private final EpicMapper epicMapper;
    private final SubtaskMapper subtaskMapper;
    private final AbstractMapper<Task, TaskDto> abstractMapper;

    public TaskManagerImpl(
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

        abstractMapper.put(Task.class, value -> taskMapper.toDto((Task) value));
        abstractMapper.put(Subtask.class, value -> subtaskMapper.toDto((Subtask) value));
        abstractMapper.put(Epic.class, value -> this.getEpicDtoWithEpicEntity((Epic) value));
    }

    public TaskManagerImpl(TaskManagerRepository repository, HistoryManager historyManager) {
        this(new IdentifierGenerator(), repository, historyManager);
    }

    private int getNextTaskId() {
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
    public TaskDto createTask(CreateTaskDto createTaskDto) {
        Task entity = taskMapper.toEntity(getNextTaskId(), createTaskDto);
        repository.saveTask(entity);
        return taskMapper.toDto(entity);
    }

    @Override
    public TaskDto updateTask(UpdateTask updateTask) {
        Task entity = repository.getTask(updateTask.getId());
        if (entity == null) {
            return null;
        }
        taskMapper.updateEntityFromDto(updateTask, entity);
        return taskMapper.toDto(entity);
    }

    @Override
    public TaskDto getTask(int taskId) {
        Task entity = repository.getTask(taskId);
        if (entity == null) {
            return null;
        }
        historyManager.add(entity);
        return taskMapper.toDto(entity);
    }

    @Override
    public void removeTask(int taskId) {
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
    public EpicDto getEpic(int epicId) {
        Epic epic = repository.getEpic(epicId);
        if (epic == null) {
            return null;
        }
        historyManager.add(epic);
        return getEpicDtoWithEpicEntity(epic);
    }

    @Override
    public EpicDto createEpic(CreateEpicDto epicDto) {
        Epic epic = epicMapper.toEntity(getNextTaskId(), epicDto);
        repository.saveEpic(epic);
        return getEpicDtoWithEpicEntity(epic);
    }

    @Override
    public EpicDto updateEpic(UpdateEpic updateEpic) {
        Epic epic = repository.getEpic(updateEpic.getId());
        if (epic == null) {
            return null;
        }
        epicMapper.updateEntityFromDto(updateEpic, epic);
        return getEpicDtoWithEpicEntity(epic);
    }

    @Override
    public void removeEpic(int epicId) {
        Epic entity = repository.getEpic(epicId);
        if (entity == null) {
            return;
        }
        for (Integer subtaskId : entity.subtasks) {
            removeSubtaskFromRepository(subtaskId);
        }
        removeEpicFromRepository(entity.getId());
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
    public SubtaskDto getSubtask(int subtaskId) {
        Subtask entity = repository.getSubtask(subtaskId);
        if (entity == null) {
            return null;
        }
        historyManager.add(entity);
        return subtaskMapper.toDto(entity);
    }

    @Override
    public List<SubtaskDto> getSubtasksWithEpicId(int epicId) {
        Epic entity = repository.getEpic(epicId);
        if (entity == null) {
            return new ArrayList<>();
        }
        return entity.subtasks.stream()
                .map(repository::getSubtask)
                .map(subtaskMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public SubtaskDto createSubtask(CreateSubtaskDto subtaskDto) {
        int epicId = subtaskDto.getEpicId();
        Epic epic = repository.getEpic(epicId);
        if (epic == null) {
            return null;
        }
        Subtask subtask = subtaskMapper.toEntity(getNextTaskId(), subtaskDto);
        subtask.setEpicId(epicId);
        epic.subtasks.add(subtask.getId());

        repository.saveSubtask(subtask);
        updateEpicStatus(epic);

        return subtaskMapper.toDto(subtask);
    }

    @Override
    public SubtaskDto updateSubtask(UpdateSubtask updateSubtask) {
        Subtask subtask = repository.getSubtask(updateSubtask.getId());
        if (subtask == null) {
            return null;
        }
        Epic epic = repository.getEpic(subtask.getEpicId());
        if (epic == null) {
            return null;
        }
        subtaskMapper.updateEntityFromDto(updateSubtask, subtask);
        subtask.setEpicId(epic.getId());
        updateEpicStatus(epic);
        return subtaskMapper.toDto(subtask);
    }

    @Override
    public void removeSubtask(int subtaskId) {
        Subtask subtask = repository.getSubtask(subtaskId);
        if (subtask == null) {
            return;
        }
        removeSubtaskFromRepository(subtaskId);
        Epic epic = repository.getEpic(subtask.getEpicId());
        if (epic == null) {
            return;
        }
        epic.subtasks.remove(Integer.valueOf(subtaskId));
        updateEpicStatus(epic);
    }

    @Override
    public void removeAllSubtasks() {
        removeAllSubtaskFromRepository();
        for (Epic epic : repository.getAllEpics()) {
            epic.subtasks.clear();
            epic.status = TaskStatus.NEW;
        }
    }

    @Override
    public List<TaskDto> getHistory() {
        return historyManager.getHistory().stream()
                .map(abstractMapper::tryMap)
                .collect(Collectors.toList());
    }

    private void removeTaskFromRepository(int taskId) {
        repository.removeTask(taskId);
        historyManager.remove(taskId);
    }

    private void removeEpicFromRepository(int epicId) {
        repository.removeEpic(epicId);
        historyManager.remove(epicId);
    }

    private void removeSubtaskFromRepository(int subtaskId) {
        repository.removeSubtask(subtaskId);
        historyManager.remove(subtaskId);
    }

    private void removeAllSubtaskFromRepository() {
        for (Subtask subtask : repository.getAllSubtasks()) {
            removeSubtaskFromRepository(subtask.getId());
        }
    }

    // Helpers

    private TaskStatus calculateEpicStatusWithSubtasks(List<Subtask> subtaskEntities) {
        if (subtaskEntities.isEmpty()) {
            return TaskStatus.NEW;
        }
        int size = subtaskEntities.size();
        int newCount = 0;
        int doneCount = 0;
        for (Subtask subtask : subtaskEntities) {
            switch (subtask.status) {
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

    private void updateEpicStatus(Epic epic) {
        List<Subtask> subtasksEntities = epic.subtasks.stream()
                .map(repository::getSubtask)
                .collect(Collectors.toList());

        epic.status = calculateEpicStatusWithSubtasks(subtasksEntities);

    }

    private EpicDto getEpicDtoWithEpicEntity(Epic epic) {
        List<SubtaskDto> subtasks = getSubtasksWithEpicId(epic.getId());
        return epicMapper.toDto(epic, subtasks);
    }
}
