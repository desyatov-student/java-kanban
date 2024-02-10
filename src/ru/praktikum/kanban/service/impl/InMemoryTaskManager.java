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
import ru.praktikum.kanban.repository.Repository;
import ru.praktikum.kanban.repository.impl.TaskRepositoryInMemory;
import ru.praktikum.kanban.service.HistoryManager;
import ru.praktikum.kanban.service.TaskManager;
import ru.praktikum.kanban.util.EntityMapper;
import ru.praktikum.kanban.util.IdentifierGenerator;
import ru.praktikum.kanban.util.MappingUtils;

public class InMemoryTaskManager implements TaskManager {
    private final IdentifierGenerator identifierGenerator;
    private final Repository repository;
    private final HistoryManager historyManager;
    private EntityMapper<BaseTaskEntity, BaseTaskDto> entityMapper;

    public InMemoryTaskManager(
            IdentifierGenerator identifierGenerator,
            Repository repository,
            HistoryManager historyManager
    ) {
        this.identifierGenerator = identifierGenerator;
        this.repository = repository;
        this.historyManager = historyManager;
        this.entityMapper = new EntityMapper<>();

        entityMapper.put(TaskEntity.class, value -> MappingUtils.mapToTaskDto((TaskEntity) value));
        entityMapper.put(SubtaskEntity.class, value -> MappingUtils.mapToSubtaskDto((SubtaskEntity) value));
        entityMapper.put(EpicEntity.class, value -> this.getEpicDtoWithEpicEntity((EpicEntity) value));

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
                .map(MappingUtils::mapToTaskDto)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDto createTask(CreateTaskDto createTaskDto) {
        TaskEntity simpleTaskEntity = MappingUtils.mapToTaskEntity(createTaskDto, getNextTaskId());
        repository.saveTask(simpleTaskEntity);
        return MappingUtils.mapToTaskDto(simpleTaskEntity);
    }

    @Override
    public TaskDto updateTask(UpdateTaskDto updateTaskDto) {
        TaskEntity simpleTaskEntity = MappingUtils.mapToTaskEntity(updateTaskDto);
        repository.saveTask(simpleTaskEntity);
        return MappingUtils.mapToTaskDto(simpleTaskEntity);
    }

    @Override
    public TaskDto getTask(int taskId) {
        TaskEntity simpleTaskEntity = repository.getTask(taskId);
        TaskDto taskDto = MappingUtils.mapToTaskDto(simpleTaskEntity);
        historyManager.add(simpleTaskEntity);
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
        EpicEntity epicEntity = MappingUtils.mapToEpicEntity(epicDto, getNextTaskId());
        repository.saveEpic(epicEntity);
        return getEpicDtoWithEpicEntity(epicEntity);
    }

    @Override
    public EpicDto updateEpic(UpdateEpicDto updateEpicDto) {
        EpicEntity epicEntity = repository.getEpic(updateEpicDto.getId());
        EpicEntity newEpicEntity = MappingUtils.updateEpicEntity(epicEntity, updateEpicDto);
        repository.saveEpic(newEpicEntity);
        return getEpicDtoWithEpicEntity(newEpicEntity);
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
                .map(MappingUtils::mapToSubtaskDto)
                .collect(Collectors.toList());
    }

    @Override
    public SubtaskDto getSubtask(int subtaskId) {
        SubtaskEntity subtaskEntity = repository.getSubtask(subtaskId);
        SubtaskDto subtaskDto = MappingUtils.mapToSubtaskDto(subtaskEntity);
        historyManager.add(subtaskEntity);
        return subtaskDto;
    }

    @Override
    public List<SubtaskDto> getSubtasksWithEpicId(int epicId) {
        EpicEntity epicEntity = repository.getEpic(epicId);
        return epicEntity.subtasks.stream()
                .map(repository::getSubtask)
                .map(MappingUtils::mapToSubtaskDto)
                .collect(Collectors.toList());
    }

    @Override
    public SubtaskDto createSubtask(CreateSubtaskDto subtaskDto) {
        int epicId = subtaskDto.getEpicId();
        SubtaskEntity subtaskEntity = MappingUtils.mapToSubtaskEntity(subtaskDto, getNextTaskId());
        subtaskEntity.setEpicId(epicId);
        repository.saveSubtask(subtaskEntity);

        EpicEntity epicEntity = repository.getEpic(epicId);
        epicEntity.subtasks.add(subtaskEntity.getId());
        updateEpicStatus(epicEntity);

        return MappingUtils.mapToSubtaskDto(subtaskEntity);
    }

    @Override
    public SubtaskDto updateSubtask(UpdateSubtaskDto updateSubtaskDto) {
        SubtaskEntity subtaskEntity = repository.getSubtask(updateSubtaskDto.getId());
        MappingUtils.updateSubtaskEntity(subtaskEntity, updateSubtaskDto);
        EpicEntity epicEntity = repository.getEpic(subtaskEntity.getEpicId());
        subtaskEntity.setEpicId(epicEntity.getId());
        updateEpicStatus(epicEntity);
        return MappingUtils.mapToSubtaskDto(subtaskEntity);
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
                .map(entityMapper::mapToTaskDto)
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
                    return TaskStatus.IN_PROGRESS;
            }
        }

        if (newCount == size) {
            return TaskStatus.NEW;
        } else if (doneCount == size){
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
        List<SubtaskDto> subtask = getSubtasksWithEpicId(epicEntity.getId());
        return MappingUtils.mapToEpicDto(epicEntity, subtask);
    }
}
