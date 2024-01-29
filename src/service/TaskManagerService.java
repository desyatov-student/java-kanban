package service;

import java.util.List;
import java.util.stream.Collectors;
import main.models.TaskStatus;
import main.models.dto.CreateEpicDto;
import main.models.dto.CreateSubtaskDto;
import main.models.dto.EpicDto;
import main.models.dto.SubtaskDto;
import main.models.dto.TaskDto;
import main.models.dto.UpdateEpicDto;
import main.models.dto.UpdateSubtaskDto;
import main.models.dto.UpdateTaskDto;
import main.models.entity.EpicEntity;
import main.models.entity.SubtaskEntity;
import main.models.entity.TaskEntity;
import main.repository.Repository;
import main.repository.SubtaskRepository;
import main.repository.TaskRepository;
import main.utils.IdentifierGenerator;
import main.utils.MappingUtils;

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

    public void createTask(TaskDto taskDto) {
        TaskEntity taskEntity = mappingUtils.mapToTaskEntity(taskDto);
        repository.saveTask(taskEntity);
    }

    public void updateTask(UpdateTaskDto updateTaskDto) {

    }

    // Epic's methods
    public List<EpicDto> getAllEpics() {
        List<EpicEntity> epics = repository.getAllEpics();
        return epics.stream()
                .map(this::getEpicDtoWithEpicsEntity)
                .collect(Collectors.toList());
    }

    public void createEpic(CreateEpicDto epicDto) {
        EpicEntity epicEntity = mappingUtils.mapToEpicEntity(epicDto);
        repository.saveEpic(epicEntity);
    }

    public void updateEpic(UpdateEpicDto epicDto) {
        EpicEntity epicEntity = repository.getEpicWithId(epicDto.getId());
        EpicEntity newEpicEntity = mappingUtils.mapToEpicEntity(epicDto, epicEntity.getStatus());
        repository.saveEpic(newEpicEntity);
    }

    public List<SubtaskDto> getAllSubtasks() {
        return repository.getAllSubtasks().stream()
                .map(mappingUtils::mapToSubtaskDto)
                .collect(Collectors.toList());
    }

    public List<SubtaskDto> getSubtasksWithEpicDto(int epicId) {
        return getSubtasksDtosWithEpicId(epicId);
    }

    public void createSubtask(CreateSubtaskDto subtaskDto, int epicId) {
        SubtaskEntity subtaskEntity = mappingUtils.mapToSubtaskEntity(subtaskDto);
        updateEpicWithSubtaskEntity(epicId, subtaskEntity);
    }

    public void updateSubtask(UpdateSubtaskDto subtaskDto) {
        EpicEntity epicEntity = repository.getEpicWithSubtaskId(subtaskDto.getId());
        SubtaskEntity subtaskEntity = mappingUtils.mapToSubtaskEntity(subtaskDto);
        updateEpicWithSubtaskEntity(epicEntity.getId(), subtaskEntity);
    }

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

    private void updateEpicWithSubtaskEntity(int epicId, SubtaskEntity subtaskEntity) {
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

    private void updateEpicWithSubtasks(int epicId, List<SubtaskEntity> subtasks) {
        TaskStatus newStatus = calculateEpicStatusWithSubtasks(subtasks);
        EpicEntity epicEntity = mappingUtils.mapToEpicEntity(repository.getEpicWithId(epicId), newStatus);
        repository.saveEpic(epicEntity);
    }

    private EpicDto getEpicDtoWithEpicsEntity(EpicEntity epicEntity) {
        List<SubtaskDto> subtask = getSubtasksDtosWithEpicId(epicEntity.getId());
        return mappingUtils.mapToEpicDto(epicEntity, subtask);
    }

    private List<SubtaskDto> getSubtasksDtosWithEpicId(int epicId) {
        List<SubtaskDto> subtask = repository.getSubtasksWithEpicId(epicId).stream()
                .map(mappingUtils::mapToSubtaskDto)
                .collect(Collectors.toList());
        return subtask;
    }
}
