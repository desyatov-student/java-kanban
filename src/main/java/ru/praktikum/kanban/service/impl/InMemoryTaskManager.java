package ru.praktikum.kanban.service.impl;

import java.util.ArrayList;
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
        TaskEntity entity = repository.getTask(updateTaskDto.getId());
        if (entity == null) {
            return null;
        }
        taskMapper.updateEntityFromDto(updateTaskDto, entity);
        return taskMapper.toDto(entity);
    }

    @Override
    public TaskDto getTask(int taskId) {
        TaskEntity entity = repository.getTask(taskId);
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
        for (TaskEntity task : repository.getAllTasks()) {
            removeTaskFromRepository(task.getId());
        }
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
        if (epicEntity == null) {
            return null;
        }
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
        if (epicEntity == null) {
            return null;
        }
        epicMapper.updateEntityFromDto(updateEpicDto, epicEntity);
        return getEpicDtoWithEpicEntity(epicEntity);
    }

    @Override
    public void removeEpic(int epicId) {
        EpicEntity entity = repository.getEpic(epicId);
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
        for (EpicEntity epic : repository.getAllEpics()) {
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
        SubtaskEntity entity = repository.getSubtask(subtaskId);
        if (entity == null) {
            return null;
        }
        historyManager.add(entity);
        return subtaskMapper.toDto(entity);
    }

    @Override
    public List<SubtaskDto> getSubtasksWithEpicId(int epicId) {
        EpicEntity entity = repository.getEpic(epicId);
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
        EpicEntity epicEntity = repository.getEpic(epicId);
        if (epicEntity == null) {
            return null;
        }
        SubtaskEntity subtaskEntity = subtaskMapper.toEntity(getNextTaskId(), subtaskDto);
        subtaskEntity.setEpicId(epicId);
        epicEntity.subtasks.add(subtaskEntity.getId());

        repository.saveSubtask(subtaskEntity);
        updateEpicStatus(epicEntity);

        return subtaskMapper.toDto(subtaskEntity);
    }

    @Override
    public SubtaskDto updateSubtask(UpdateSubtaskDto updateSubtaskDto) {
        SubtaskEntity subtaskEntity = repository.getSubtask(updateSubtaskDto.getId());
        if (subtaskEntity == null) {
            return null;
        }
        EpicEntity epicEntity = repository.getEpic(subtaskEntity.getEpicId());
        if (epicEntity == null) {
            return null;
        }
        subtaskMapper.updateEntityFromDto(updateSubtaskDto, subtaskEntity);
        subtaskEntity.setEpicId(epicEntity.getId());
        updateEpicStatus(epicEntity);
        return subtaskMapper.toDto(subtaskEntity);
    }

    @Override
    public void removeSubtask(int subtaskId) {
        SubtaskEntity subtaskEntity = repository.getSubtask(subtaskId);
        if (subtaskEntity == null) {
            return;
        }
        removeSubtaskFromRepository(subtaskId);
        EpicEntity epicEntity = repository.getEpic(subtaskEntity.getEpicId());
        if (epicEntity == null) {
            return;
        }
        epicEntity.subtasks.remove(Integer.valueOf(subtaskId));
        updateEpicStatus(epicEntity);
    }

    @Override
    public void removeAllSubtasks() {
        removeAllSubtaskFromRepository();
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
        for (SubtaskEntity subtask : repository.getAllSubtasks()) {
            removeSubtaskFromRepository(subtask.getId());
        }
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
