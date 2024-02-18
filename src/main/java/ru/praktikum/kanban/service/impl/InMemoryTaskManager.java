package ru.praktikum.kanban.service.impl;

import java.util.List;
import java.util.stream.Collectors;
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
import ru.praktikum.kanban.model.entity.BaseTaskEntity;
import ru.praktikum.kanban.model.entity.EpicEntity;
import ru.praktikum.kanban.model.entity.SubtaskEntity;
import ru.praktikum.kanban.model.entity.TaskEntity;
import ru.praktikum.kanban.model.mapper.EpicMapper;
import ru.praktikum.kanban.model.mapper.EpicMapperImpl;
import ru.praktikum.kanban.model.mapper.SubtaskMapper;
import ru.praktikum.kanban.model.mapper.SubtaskMapperImpl;
import ru.praktikum.kanban.model.mapper.TaskMapper;
import ru.praktikum.kanban.model.mapper.TaskMapperImpl;
import ru.praktikum.kanban.repository.Repository;
import ru.praktikum.kanban.repository.impl.TaskRepositoryInMemory;
import ru.praktikum.kanban.service.HistoryManager;
import ru.praktikum.kanban.service.TaskManager;
import ru.praktikum.kanban.util.AbstractMapper;
import ru.praktikum.kanban.util.IdentifierGenerator;

public class InMemoryTaskManager implements TaskManager {
    private final IdentifierGenerator identifierGenerator;
    private final Repository repository;
    private final HistoryManager historyManager;
    private final AbstractMapper<BaseTaskEntity, BaseTaskDto> abstractMapper;
    private final TaskMapper taskMapper;
    private final EpicMapper epicMapper;
    private final SubtaskMapper subtaskMapper;

    public InMemoryTaskManager(
            IdentifierGenerator identifierGenerator,
            Repository repository,
            HistoryManager historyManager
    ) {
        this.identifierGenerator = identifierGenerator;
        this.repository = repository;
        this.historyManager = historyManager;
        this.abstractMapper = new AbstractMapper<>();
        this.taskMapper = new TaskMapperImpl();
        this.epicMapper = new EpicMapperImpl();
        this.subtaskMapper = new SubtaskMapperImpl();

        abstractMapper.put(TaskEntity.class, value -> taskMapper.toDto((TaskEntity) value));
        abstractMapper.put(SubtaskEntity.class, value -> subtaskMapper.toDto((SubtaskEntity) value));
        abstractMapper.put(EpicEntity.class, value -> this.getEpicDtoWithEpicEntity((EpicEntity) value));

    }

    public InMemoryTaskManager() {
        this(new IdentifierGenerator(), new TaskRepositoryInMemory(), new InMemoryHistoryManager());
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
        TaskEntity entity = taskMapper.toEntity(getNextTaskId(), createTaskDto);
        repository.saveTask(entity);
        return taskMapper.toDto(entity);
    }

    @Override
    public TaskDto updateTask(UpdateTaskDto updateTaskDto) {
        TaskEntity taskEntity = repository.getTask(updateTaskDto.getId());
        taskMapper.updateEntityFromDto(updateTaskDto, taskEntity);
        return taskMapper.toDto(taskEntity);
    }

    @Override
    public TaskDto getTask(int taskId) {
        TaskEntity entity = repository.getTask(taskId);
        TaskDto taskDto = taskMapper.toDto(entity);
        historyManager.add(entity);
        return taskDto;
    }

    @Override
    public void removeTask(int taskId) {
        repository.removeTask(taskId);
    }

    @Override
    public void removeAllTasks() {
        repository.removeAllTasks();
    }

    // Epic's methods

    @Override
    public List<EpicDto> getAllEpics() {
        List<EpicEntity> epics = repository.getAllEpics();
        return epics.stream()
                .map(this::getEpicDtoWithEpicEntity)
                .collect(Collectors.toList());
    }

    @Override
    public EpicDto getEpic(int epicId) {
        EpicEntity epicEntity = repository.getEpic(epicId);
        EpicDto epicDto = getEpicDtoWithEpicEntity(epicEntity);
        historyManager.add(epicEntity);
        return epicDto;
    }

    @Override
    public EpicDto createEpic(CreateEpicDto epicDto) {
        EpicEntity epicEntity = epicMapper.toEntity(getNextTaskId(), epicDto);
        repository.saveEpic(epicEntity);
        return getEpicDtoWithEpicEntity(epicEntity);
    }

    @Override
    public EpicDto updateEpic(UpdateEpicDto updateEpicDto) {
        EpicEntity epicEntity = repository.getEpic(updateEpicDto.getId());
        epicMapper.updateEntityFromDto(updateEpicDto, epicEntity);
        return getEpicDtoWithEpicEntity(epicEntity);
    }

    @Override
    public void removeEpic(int epicId) {
        EpicEntity epicEntity = repository.getEpic(epicId);
        repository.removeEpic(epicEntity);
    }

    @Override
    public void removeAllEpics() {
        repository.removeAllEpics();
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
        SubtaskEntity subtaskEntity = repository.getSubtask(subtaskId);
        historyManager.add(subtaskEntity);
        return subtaskMapper.toDto(subtaskEntity);
    }

    @Override
    public List<SubtaskDto> getSubtasksWithEpicId(int epicId) {
        EpicEntity epicEntity = repository.getEpic(epicId);
        return epicEntity.subtasks.stream()
                .map(repository::getSubtask)
                .map(subtaskMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public SubtaskDto createSubtask(CreateSubtaskDto subtaskDto) {
        int epicId = subtaskDto.getEpicId();
        SubtaskEntity subtaskEntity = subtaskMapper.toEntity(getNextTaskId(), subtaskDto);
        subtaskEntity.setEpicId(epicId);
        repository.saveSubtask(subtaskEntity);

        EpicEntity epicEntity = repository.getEpic(epicId);
        epicEntity.subtasks.add(subtaskEntity.getId());
        updateEpicStatus(epicEntity);

        return subtaskMapper.toDto(subtaskEntity);
    }

    @Override
    public SubtaskDto updateSubtask(UpdateSubtaskDto updateSubtaskDto) {
        SubtaskEntity subtaskEntity = repository.getSubtask(updateSubtaskDto.getId());
        subtaskMapper.updateEntityFromDto(updateSubtaskDto, subtaskEntity);
        EpicEntity epicEntity = repository.getEpic(subtaskEntity.getEpicId());
        subtaskEntity.setEpicId(epicEntity.getId());
        updateEpicStatus(epicEntity);
        return subtaskMapper.toDto(subtaskEntity);
    }

    @Override
    public void removeSubtask(int subtaskId) {
        SubtaskEntity subtaskEntity = repository.getSubtask(subtaskId);
        EpicEntity epicEntity = repository.getEpic(subtaskEntity.getEpicId());
        repository.removeSubtask(subtaskId);
        epicEntity.subtasks.remove(Integer.valueOf(subtaskId));
        updateEpicStatus(epicEntity);
    }

    @Override
    public void removeAllSubtasks() {
        repository.removeAllSubtasks();
        for (EpicEntity epic : repository.getAllEpics()) {
            epic.subtasks.clear();
            epic.status = TaskStatus.NEW;
        }
    }

    @Override
    public List<BaseTaskDto> getHistory() {
        return historyManager.getHistory().stream()
                .map(abstractMapper::tryMap)
                .collect(Collectors.toList());
    }

    // Helpers

    private TaskStatus calculateEpicStatusWithSubtasks(List<SubtaskEntity> subtaskEntities) {
        if (subtaskEntities.isEmpty()) {
            return TaskStatus.NEW;
        }
        int size = subtaskEntities.size();
        int newCount = 0;
        int doneCount = 0;
        for (SubtaskEntity subtaskEntity : subtaskEntities) {
            switch (subtaskEntity.status) {
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

    private void updateEpicStatus(EpicEntity epicEntity) {
        List<SubtaskEntity> subtasksEntities = epicEntity.subtasks.stream()
                .map(repository::getSubtask)
                .collect(Collectors.toList());

        epicEntity.status = calculateEpicStatusWithSubtasks(subtasksEntities);

    }

    private EpicDto getEpicDtoWithEpicEntity(EpicEntity epicEntity) {
        List<SubtaskDto> subtasks = getSubtasksWithEpicId(epicEntity.getId());
        return epicMapper.toDto(epicEntity, subtasks);
    }
}