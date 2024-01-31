package main.service;

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

    public int getNextTaskId() {
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
        EpicEntity epicEntity = repository.getEpicWithId(epicId);
        return getEpicWithEpicEntity(epicEntity);
    }

    public EpicDto createEpic(CreateEpicDto epicDto) {
        EpicEntity epicEntity = mappingUtils.mapToEpicEntity(epicDto);
        repository.saveEpic(epicEntity);
        return getEpicWithEpicEntity(epicEntity);
    }

    public EpicDto updateEpic(UpdateEpicDto epicDto) {
        EpicEntity epicEntity = repository.getEpicWithId(epicDto.getId());
        EpicEntity newEpicEntity = mappingUtils.mapToEpicEntity(epicDto, epicEntity.getStatus());
        repository.saveEpic(newEpicEntity);
        return getEpicWithEpicEntity(newEpicEntity);
    }

    public void removeEpic(int epicId) {
        repository.removeEpic(epicId);
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

    private List<SubtaskDto> getSubtasksWithEpicId(int epicId) {
        List<SubtaskDto> subtask = repository.getSubtasksWithEpicId(epicId).stream()
                .map(mappingUtils::mapToSubtaskDto)
                .collect(Collectors.toList());
        return subtask;
    }

    public SubtaskDto createSubtask(CreateSubtaskDto subtaskDto, int epicId) {
        SubtaskEntity subtaskEntity = mappingUtils.mapToSubtaskEntity(subtaskDto);
        updateEpicWithSubtask(epicId, subtaskEntity);
        return mappingUtils.mapToSubtaskDto(subtaskEntity);
    }

    public SubtaskDto updateSubtask(UpdateSubtaskDto subtaskDto) {
        EpicEntity epicEntity = repository.getEpicWithSubtaskId(subtaskDto.getId());
        SubtaskEntity subtaskEntity = mappingUtils.mapToSubtaskEntity(subtaskDto);
        updateEpicWithSubtask(epicEntity.getId(), subtaskEntity);
        return mappingUtils.mapToSubtaskDto(subtaskEntity);
    }

    public void removeSubtask(int subtaskId) {
        EpicEntity epicEntity = repository.getEpicWithSubtaskId(subtaskId);
        repository.removeSubtask(subtaskId);
        List<SubtaskEntity> subtasks = repository.getSubtasksWithEpicId(epicEntity.getId());
        updateEpicStatusWithSubtasks(epicEntity, subtasks);
    }

    public void removeAllSubtasks() {
        repository.removeAllSubtasks();
        for (EpicEntity epic : repository.getAllEpics()) {
            repository.saveEpic(mappingUtils.mapToEpicEntity(epic, TaskStatus.NEW));
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

    private void updateEpicWithSubtask(int epicId, SubtaskEntity subtaskEntity) {
        List<SubtaskEntity> subtasksEntities = repository.getSubtasksWithEpicId(epicId);

        List<Integer> subtasksIds = subtasksEntities.stream().map(SubtaskEntity::getId).collect(Collectors.toList());
        int index = subtasksIds.indexOf(subtaskEntity.getId());
        if (index >= 0) {
            subtasksEntities.remove(index);
            subtasksEntities.add(index, subtaskEntity);
        } else {
            subtasksEntities.add(subtaskEntity);
        }

        TaskStatus newStatus = calculateEpicStatusWithSubtasks(subtasksEntities);
        EpicEntity epicEntity = mappingUtils.mapToEpicEntity(repository.getEpicWithId(epicId), newStatus);
        repository.saveEpic(epicEntity, subtasksEntities);
    }

    private void updateEpicStatusWithSubtasks(EpicEntity epicEntity, List<SubtaskEntity> subtasks) {
        TaskStatus newStatus = calculateEpicStatusWithSubtasks(subtasks);
        EpicEntity newEpicEntity = mappingUtils.mapToEpicEntity(epicEntity, newStatus);
        repository.saveEpic(newEpicEntity);
    }

    private EpicDto getEpicWithEpicEntity(EpicEntity epicEntity) {
        List<SubtaskDto> subtask = getSubtasksWithEpicId(epicEntity.getId());
        return mappingUtils.mapToEpicDto(epicEntity, subtask);
    }
}
