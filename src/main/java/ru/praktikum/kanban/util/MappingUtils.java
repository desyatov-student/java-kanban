package ru.praktikum.kanban.util;

import java.util.List;
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

public class MappingUtils {

    public static TaskDto mapToTaskDto(TaskEntity entity) {
        TaskDto dto = new TaskDto(
                entity.getId(),
                entity.name,
                entity.description,
                entity.getStatus()
        );
        return dto;
    }

    public static TaskEntity mapToTaskEntity(UpdateTaskDto updateTaskDto) {
        TaskEntity entity = new TaskEntity(
                updateTaskDto.getId(),
                updateTaskDto.getName(),
                updateTaskDto.getDescription(),
                updateTaskDto.getStatus()
        );
        return entity;
    }

    public static TaskEntity mapToTaskEntity(CreateTaskDto createTaskDto, int taskId) {
        TaskEntity entity = new TaskEntity(
                taskId,
                createTaskDto.getName(),
                createTaskDto.getDescription(),
                createTaskDto.getStatus()
        );
        return entity;
    }

    public static EpicDto mapToEpicDto(EpicEntity epicEntity, List<SubtaskDto> subtasks) {
        EpicDto dto = new EpicDto(
                epicEntity.getId(),
                epicEntity.name,
                epicEntity.description,
                epicEntity.getStatus(),
                subtasks
        );
        return dto;
    }

    public static EpicEntity mapToEpicEntity(CreateEpicDto createEpicDto, int epicId) {
        EpicEntity entity = new EpicEntity(
                epicId,
                createEpicDto.getName(),
                createEpicDto.getDescription(),
                TaskStatus.NEW
        );
        return entity;
    }

    public static EpicEntity updateEpicEntity(EpicEntity epicEntity, UpdateEpicDto updateEpicDto) {
        epicEntity.name = updateEpicDto.getName();
        epicEntity.description = updateEpicDto.getDescription();
        return epicEntity;
    }

    public static SubtaskDto mapToSubtaskDto(SubtaskEntity subtaskEntity) {
        SubtaskDto dto = new SubtaskDto(
                subtaskEntity.getId(),
                subtaskEntity.name,
                subtaskEntity.description,
                subtaskEntity.getStatus()
        );
        return dto;
    }

    public static SubtaskEntity updateSubtaskEntity(SubtaskEntity subtaskEntity, UpdateSubtaskDto updateSubtaskDto) {
        subtaskEntity.name = updateSubtaskDto.getName();
        subtaskEntity.description = updateSubtaskDto.getDescription();
        subtaskEntity.status = updateSubtaskDto.getStatus();
        return subtaskEntity;
    }

    public static SubtaskEntity mapToSubtaskEntity(CreateSubtaskDto createSubtaskDto, int subtaskId) {
        SubtaskEntity entity = new SubtaskEntity(
                subtaskId,
                createSubtaskDto.getName(),
                createSubtaskDto.getDescription(),
                createSubtaskDto.getStatus()
        );
        return entity;
    }
}
