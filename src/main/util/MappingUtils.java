package main.util;

import java.util.List;
import main.model.TaskStatus;
import main.model.dto.CreateEpicDto;
import main.model.dto.CreateSubtaskDto;
import main.model.dto.CreateTaskDto;
import main.model.dto.EpicDto;
import main.model.dto.SubtaskDto;
import main.model.dto.TaskDto;
import main.model.dto.UpdateEpicDto;
import main.model.dto.UpdateSubtaskDto;
import main.model.dto.UpdateTaskDto;
import main.model.entity.EpicEntity;
import main.model.entity.SubtaskEntity;
import main.model.entity.TaskEntity;

public class MappingUtils {

    public TaskDto mapToTaskDto(TaskEntity entity){
        TaskDto dto = new TaskDto(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getStatus()
        );
        return dto;
    }

    public TaskEntity mapToTaskEntity(UpdateTaskDto updateTaskDto){
        TaskEntity entity = new TaskEntity(
                updateTaskDto.getId(),
                updateTaskDto.getName(),
                updateTaskDto.getDescription(),
                updateTaskDto.getStatus()
        );
        return entity;
    }

    public TaskEntity mapToTaskEntity(CreateTaskDto createTaskDto){
        TaskEntity entity = new TaskEntity(
                createTaskDto.getId(),
                createTaskDto.getName(),
                createTaskDto.getDescription(),
                createTaskDto.getStatus()
        );
        return entity;
    }

    public EpicDto mapToEpicDto(EpicEntity epicEntity, List<SubtaskDto> subtasks){
        EpicDto dto = new EpicDto(
                epicEntity.getId(),
                epicEntity.getName(),
                epicEntity.getDescription(),
                epicEntity.getStatus(),
                subtasks
        );
        return dto;
    }

    public EpicEntity mapToEpicEntity(EpicDto epicDto){
        EpicEntity entity = new EpicEntity(
                epicDto.getId(),
                epicDto.getName(),
                epicDto.getDescription(),
                epicDto.getStatus()
        );
        return entity;
    }

    public EpicEntity mapToEpicEntity(CreateEpicDto createEpicDto){
        EpicEntity entity = new EpicEntity(
                createEpicDto.getId(),
                createEpicDto.getName(),
                createEpicDto.getDescription(),
                TaskStatus.NEW
        );
        return entity;
    }

    public EpicEntity mapToEpicEntity(EpicEntity epicEntity, TaskStatus newStatus){
        EpicEntity entity = new EpicEntity(
                epicEntity.getId(),
                epicEntity.getName(),
                epicEntity.getDescription(),
                newStatus
        );
        return entity;
    }

    public EpicEntity mapToEpicEntity(UpdateEpicDto updateEpicDto, TaskStatus newStatus){
        EpicEntity entity = new EpicEntity(
                updateEpicDto.getId(),
                updateEpicDto.getName(),
                updateEpicDto.getDescription(),
                newStatus
        );
        return entity;
    }

    public SubtaskDto mapToSubtaskDto(SubtaskEntity subtaskEntity){
        SubtaskDto dto = new SubtaskDto(
                subtaskEntity.getId(),
                subtaskEntity.getName(),
                subtaskEntity.getDescription(),
                subtaskEntity.getStatus()
        );
        return dto;
    }

    public SubtaskEntity mapToSubtaskEntity(UpdateSubtaskDto updateSubtaskDto){
        SubtaskEntity entity = new SubtaskEntity(
                updateSubtaskDto.getId(),
                updateSubtaskDto.getName(),
                updateSubtaskDto.getDescription(),
                updateSubtaskDto.getStatus()
        );
        return entity;
    }

    public SubtaskEntity mapToSubtaskEntity(CreateSubtaskDto createSubtaskDto){
        SubtaskEntity entity = new SubtaskEntity(
                createSubtaskDto.getId(),
                createSubtaskDto.getName(),
                createSubtaskDto.getDescription(),
                createSubtaskDto.getStatus()
        );
        return entity;
    }
}
