package main.java.ru.praktikum.kanban.util;

import java.util.List;
import main.java.ru.praktikum.kanban.model.dto.create.CreateEpicDto;
import main.java.ru.praktikum.kanban.model.dto.create.CreateSubtaskDto;
import main.java.ru.praktikum.kanban.model.TaskStatus;
import main.java.ru.praktikum.kanban.model.dto.create.CreateTaskDto;
import main.java.ru.praktikum.kanban.model.dto.response.EpicDto;
import main.java.ru.praktikum.kanban.model.dto.response.SubtaskDto;
import main.java.ru.praktikum.kanban.model.dto.response.TaskDto;
import main.java.ru.praktikum.kanban.model.dto.update.UpdateEpicDto;
import main.java.ru.praktikum.kanban.model.dto.update.UpdateSubtaskDto;
import main.java.ru.praktikum.kanban.model.dto.update.UpdateTaskDto;
import main.java.ru.praktikum.kanban.model.entity.EpicEntity;
import main.java.ru.praktikum.kanban.model.entity.SubtaskEntity;
import main.java.ru.praktikum.kanban.model.entity.TaskEntity;

public class MappingUtils {

    static public TaskDto mapToTaskDto(TaskEntity entity){
        TaskDto dto = new TaskDto(
                entity.getId(),
                entity.name,
                entity.description,
                entity.getStatus()
        );
        return dto;
    }

    static public TaskEntity mapToTaskEntity(UpdateTaskDto updateTaskDto){
        TaskEntity entity = new TaskEntity(
                updateTaskDto.getId(),
                updateTaskDto.getName(),
                updateTaskDto.getDescription(),
                updateTaskDto.getStatus()
        );
        return entity;
    }

    static public TaskEntity mapToTaskEntity(CreateTaskDto createTaskDto, int taskId){
        TaskEntity entity = new TaskEntity(
                taskId,
                createTaskDto.getName(),
                createTaskDto.getDescription(),
                createTaskDto.getStatus()
        );
        return entity;
    }

    static public EpicDto mapToEpicDto(EpicEntity epicEntity, List<SubtaskDto> subtasks){
        EpicDto dto = new EpicDto(
                epicEntity.getId(),
                epicEntity.name,
                epicEntity.description,
                epicEntity.getStatus(),
                subtasks
        );
        return dto;
    }

    static public EpicEntity mapToEpicEntity(CreateEpicDto createEpicDto, int epicId){
        EpicEntity entity = new EpicEntity(
                epicId,
                createEpicDto.getName(),
                createEpicDto.getDescription(),
                TaskStatus.NEW
        );
        return entity;
    }

    static public EpicEntity updateEpicEntity(EpicEntity epicEntity, UpdateEpicDto updateEpicDto){
        epicEntity.name = updateEpicDto.getName();
        epicEntity.description = updateEpicDto.getDescription();
        return epicEntity;
    }

    static public SubtaskDto mapToSubtaskDto(SubtaskEntity subtaskEntity){
        SubtaskDto dto = new SubtaskDto(
                subtaskEntity.getId(),
                subtaskEntity.name,
                subtaskEntity.description,
                subtaskEntity.getStatus()
        );
        return dto;
    }

    static public SubtaskEntity updateSubtaskEntity(SubtaskEntity subtaskEntity, UpdateSubtaskDto updateSubtaskDto){
        subtaskEntity.name = updateSubtaskDto.getName();
        subtaskEntity.description = updateSubtaskDto.getDescription();
        subtaskEntity.status = updateSubtaskDto.getStatus();
        return subtaskEntity;
    }

    static public SubtaskEntity mapToSubtaskEntity(CreateSubtaskDto createSubtaskDto, int subtaskId){
        SubtaskEntity entity = new SubtaskEntity(
                subtaskId,
                createSubtaskDto.getName(),
                createSubtaskDto.getDescription(),
                createSubtaskDto.getStatus()
        );
        return entity;
    }
}
