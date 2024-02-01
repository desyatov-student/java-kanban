package main.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
import main.model.entity.EpicEntity;
import main.model.entity.SubtaskEntity;
import main.model.entity.TaskEntity;
import main.repository.Repository;
import main.util.IdentifierGenerator;
import main.util.MappingUtils;

public class TaskManagerService {
    private final IdentifierGenerator identifierGenerator;
    private final Repository repository;
    private final MappingUtils mappingUtils;

    public TaskManagerService(IdentifierGenerator identifierGenerator, Repository repository, MappingUtils mappingUtils) {
        this.identifierGenerator = identifierGenerator;
        this.repository = repository;
        this.mappingUtils = mappingUtils;
    }

    private int getNextTaskId() {
        return identifierGenerator.getNextId();
    }

    // Task's methods

    public List<TaskDto> getAllTasks() {
        return repository.getAllTasks().stream()
                .map(mappingUtils::mapToTaskDto)
                .collect(Collectors.toList());
    }

    public TaskDto createTask(CreateTaskDto createTaskDto) {
        TaskEntity taskEntity = mappingUtils.mapToTaskEntity(createTaskDto, getNextTaskId());
        repository.saveTask(taskEntity);
        return mappingUtils.mapToTaskDto(taskEntity);
    }

    public TaskDto updateTask(UpdateTaskDto updateTaskDto) {
        TaskEntity taskEntity = mappingUtils.mapToTaskEntity(updateTaskDto);
        repository.saveTask(taskEntity);
        return mappingUtils.mapToTaskDto(taskEntity);
    }

    public TaskDto getTask(int taskId) {
        return mappingUtils.mapToTaskDto(repository.getTask(taskId));
    }
    public void removeTask(int taskId) {
        repository.removeTask(taskId);
    }
    public void removeAllTasks() {
        repository.removeAllTasks();
    }

    // Epic's methods

    public List<EpicDto> getAllEpics() {
        List<EpicEntity> epics = repository.getAllEpics();
        return epics.stream()
                .map(this::getEpicWithEpicEntity)
                .collect(Collectors.toList());
    }

    public EpicDto getEpic(int epicId) {
        EpicEntity epicEntity = repository.getEpic(epicId);
        return getEpicWithEpicEntity(epicEntity);
    }

    public EpicDto createEpic(CreateEpicDto epicDto) {
        EpicEntity epicEntity = mappingUtils.mapToEpicEntity(epicDto, getNextTaskId());
        repository.saveEpic(epicEntity);
        return getEpicWithEpicEntity(epicEntity);
    }

    public EpicDto updateEpic(UpdateEpicDto updateEpicDto) {
        EpicEntity epicEntity = repository.getEpic(updateEpicDto.getId());
        EpicEntity newEpicEntity = mappingUtils.updateEpicEntity(epicEntity, updateEpicDto);
        repository.saveEpic(newEpicEntity);
        return getEpicWithEpicEntity(newEpicEntity);
    }

    public void removeEpic(int epicId) {
        EpicEntity epicEntity = repository.getEpic(epicId);
        repository.removeEpic(epicEntity);
    }

    public void removeAllEpics() {
        repository.removeAllEpics();
    }

    // Subtask's methods

    public List<SubtaskDto> getAllSubtasks() {
        return repository.getAllSubtasks().stream()
                .map(mappingUtils::mapToSubtaskDto)
                .collect(Collectors.toList());
    }

    public SubtaskDto getSubtask(int subtaskId) {
        return mappingUtils.mapToSubtaskDto(repository.getSubtask(subtaskId));
    }

    public List<SubtaskDto> getSubtasksWithEpicId(int epicId) {
        EpicEntity epicEntity = repository.getEpic(epicId);
        List<SubtaskDto> subtask = epicEntity.subtasks.stream()
                .map(repository::getSubtask)
                .map(mappingUtils::mapToSubtaskDto)
                .collect(Collectors.toList());
        return subtask;
    }

    public SubtaskDto createSubtask(CreateSubtaskDto subtaskDto, int epicId) {
        SubtaskEntity subtaskEntity = mappingUtils.mapToSubtaskEntity(subtaskDto, getNextTaskId());
        subtaskEntity.setEpicId(epicId);
        repository.saveSubtask(subtaskEntity);

        EpicEntity epicEntity = repository.getEpic(epicId);
        epicEntity.subtasks.add(subtaskEntity.getId());
        updateEpicStatus(epicEntity);

        return mappingUtils.mapToSubtaskDto(subtaskEntity);
    }

    public SubtaskDto updateSubtask(UpdateSubtaskDto updateSubtaskDto) {
        SubtaskEntity subtaskEntity = repository.getSubtask(updateSubtaskDto.getId());
        mappingUtils.updateSubtaskEntity(subtaskEntity, updateSubtaskDto);
        EpicEntity epicEntity = repository.getEpic(subtaskEntity.getEpicId());
        subtaskEntity.setEpicId(epicEntity.getId());
        updateEpicStatus(epicEntity);
        return mappingUtils.mapToSubtaskDto(subtaskEntity);
    }

    public void removeSubtask(int subtaskId) {
        SubtaskEntity subtaskEntity = repository.getSubtask(subtaskId);
        EpicEntity epicEntity = repository.getEpic(subtaskEntity.getEpicId());
        repository.removeSubtask(subtaskId);
        epicEntity.subtasks.remove(new Integer(subtaskId));
        updateEpicStatus(epicEntity);
    }

    public void removeAllSubtasks() {
        repository.removeAllSubtasks();
        for (EpicEntity epic : repository.getAllEpics()) {
            epic.subtasks = new ArrayList<>();
            epic.status = TaskStatus.NEW;
        }
    }

    // Helpers

    private TaskStatus calculateEpicStatusWithSubtasks(List<SubtaskEntity> subtaskEntities) {
        if (subtaskEntities.isEmpty()) {
            return TaskStatus.NEW;
        }
        List<TaskStatus> taskStatuses = subtaskEntities.stream()
                .map(SubtaskEntity::getStatus)
                .collect(Collectors.toList());

        boolean isNew = taskStatuses.stream().allMatch(taskStatus -> taskStatus == TaskStatus.NEW);
        if (isNew) {
            return TaskStatus.NEW;
        }

        boolean isDone = taskStatuses.stream().allMatch(taskStatus -> taskStatus == TaskStatus.DONE);
        if (isDone) {
            return TaskStatus.DONE;
        }

        return TaskStatus.IN_PROGRESS;
    }

    private void updateEpicStatus(EpicEntity epicEntity) {
        List<SubtaskEntity> subtasksEntities = epicEntity.subtasks.stream()
                .map(repository::getSubtask)
                .collect(Collectors.toList());

        TaskStatus newStatus = calculateEpicStatusWithSubtasks(subtasksEntities);
        epicEntity.status = newStatus;

    }

    private EpicDto getEpicWithEpicEntity(EpicEntity epicEntity) {
        List<SubtaskDto> subtask = getSubtasksWithEpicId(epicEntity.getId());
        return mappingUtils.mapToEpicDto(epicEntity, subtask);
    }
}
