package ru.praktikum.kanban.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import ru.praktikum.kanban.model.TaskStatus;
import ru.praktikum.kanban.model.dto.create.CreateEpicDto;
import ru.praktikum.kanban.model.dto.create.CreateSubtaskDto;
import ru.praktikum.kanban.model.dto.create.CreateTaskDto;
import ru.praktikum.kanban.model.dto.response.EpicDto;
import ru.praktikum.kanban.model.dto.response.SubtaskDto;
import ru.praktikum.kanban.model.dto.response.TaskDto;
import ru.praktikum.kanban.model.dto.update.UpdateEpicDto;
import ru.praktikum.kanban.model.dto.update.UpdateSubtaskDto;
import ru.praktikum.kanban.model.dto.update.UpdateTaskDto;
import ru.praktikum.kanban.model.entity.EpicEntity;
import ru.praktikum.kanban.model.entity.SubtaskEntity;
import ru.praktikum.kanban.model.entity.TaskEntity;
import ru.praktikum.kanban.repository.Repository;
import ru.praktikum.kanban.service.TaskManager;
import ru.praktikum.kanban.util.IdentifierGenerator;
import ru.praktikum.kanban.util.MappingUtils;

public class InMemoryTaskManager implements TaskManager {
    private final IdentifierGenerator identifierGenerator;
    private final Repository repository;
    private final MappingUtils mappingUtils;

    public InMemoryTaskManager(IdentifierGenerator identifierGenerator, Repository repository, MappingUtils mappingUtils) {
        this.identifierGenerator = identifierGenerator;
        this.repository = repository;
        this.mappingUtils = mappingUtils;
    }

    private int getNextTaskId() {
        return identifierGenerator.getNextId();
    }

    // Task's methods

    @Override
    public List<TaskDto> getAllTasks() {
        return repository.getAllTasks().stream()
                .map(mappingUtils::mapToTaskDto)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDto createTask(CreateTaskDto createTaskDto) {
        TaskEntity taskEntity = mappingUtils.mapToTaskEntity(createTaskDto, getNextTaskId());
        repository.saveTask(taskEntity);
        return mappingUtils.mapToTaskDto(taskEntity);
    }

    @Override
    public TaskDto updateTask(UpdateTaskDto updateTaskDto) {
        TaskEntity taskEntity = mappingUtils.mapToTaskEntity(updateTaskDto);
        repository.saveTask(taskEntity);
        return mappingUtils.mapToTaskDto(taskEntity);
    }

    @Override
    public TaskDto getTask(int taskId) {
        return mappingUtils.mapToTaskDto(repository.getTask(taskId));
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
                .map(this::getEpicWithEpicEntity)
                .collect(Collectors.toList());
    }

    @Override
    public EpicDto getEpic(int epicId) {
        EpicEntity epicEntity = repository.getEpic(epicId);
        return getEpicWithEpicEntity(epicEntity);
    }

    @Override
    public EpicDto createEpic(CreateEpicDto epicDto) {
        EpicEntity epicEntity = mappingUtils.mapToEpicEntity(epicDto, getNextTaskId());
        repository.saveEpic(epicEntity);
        return getEpicWithEpicEntity(epicEntity);
    }

    @Override
    public EpicDto updateEpic(UpdateEpicDto updateEpicDto) {
        EpicEntity epicEntity = repository.getEpic(updateEpicDto.getId());
        EpicEntity newEpicEntity = mappingUtils.updateEpicEntity(epicEntity, updateEpicDto);
        repository.saveEpic(newEpicEntity);
        return getEpicWithEpicEntity(newEpicEntity);
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
                .map(mappingUtils::mapToSubtaskDto)
                .collect(Collectors.toList());
    }

    @Override
    public SubtaskDto getSubtask(int subtaskId) {
        return mappingUtils.mapToSubtaskDto(repository.getSubtask(subtaskId));
    }

    @Override
    public List<SubtaskDto> getSubtasksWithEpicId(int epicId) {
        EpicEntity epicEntity = repository.getEpic(epicId);
        List<SubtaskDto> subtask = epicEntity.subtasks.stream()
                .map(repository::getSubtask)
                .map(mappingUtils::mapToSubtaskDto)
                .collect(Collectors.toList());
        return subtask;
    }

    @Override
    public SubtaskDto createSubtask(CreateSubtaskDto subtaskDto, int epicId) {
        SubtaskEntity subtaskEntity = mappingUtils.mapToSubtaskEntity(subtaskDto, getNextTaskId());
        subtaskEntity.setEpicId(epicId);
        repository.saveSubtask(subtaskEntity);

        EpicEntity epicEntity = repository.getEpic(epicId);
        epicEntity.subtasks.add(subtaskEntity.getId());
        updateEpicStatus(epicEntity);

        return mappingUtils.mapToSubtaskDto(subtaskEntity);
    }

    @Override
    public SubtaskDto updateSubtask(UpdateSubtaskDto updateSubtaskDto) {
        SubtaskEntity subtaskEntity = repository.getSubtask(updateSubtaskDto.getId());
        mappingUtils.updateSubtaskEntity(subtaskEntity, updateSubtaskDto);
        EpicEntity epicEntity = repository.getEpic(subtaskEntity.getEpicId());
        subtaskEntity.setEpicId(epicEntity.getId());
        updateEpicStatus(epicEntity);
        return mappingUtils.mapToSubtaskDto(subtaskEntity);
    }

    @Override
    public void removeSubtask(int subtaskId) {
        SubtaskEntity subtaskEntity = repository.getSubtask(subtaskId);
        EpicEntity epicEntity = repository.getEpic(subtaskEntity.getEpicId());
        repository.removeSubtask(subtaskId);
        epicEntity.subtasks.remove(new Integer(subtaskId));
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

        TaskStatus newStatus = calculateEpicStatusWithSubtasks(subtasksEntities);
        epicEntity.status = newStatus;

    }

    private EpicDto getEpicWithEpicEntity(EpicEntity epicEntity) {
        List<SubtaskDto> subtask = getSubtasksWithEpicId(epicEntity.getId());
        return mappingUtils.mapToEpicDto(epicEntity, subtask);
    }
}
