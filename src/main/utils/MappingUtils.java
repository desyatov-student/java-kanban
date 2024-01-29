package main.utils;

import java.util.List;
import main.models.TaskStatus;
import main.models.dto.CreateEpicDto;
import main.models.dto.CreateSubtaskDto;
import main.models.dto.EpicDto;
import main.models.dto.SubtaskDto;
import main.models.dto.TaskDto;
import main.models.dto.UpdateEpicDto;
import main.models.dto.UpdateSubtaskDto;
import main.models.entity.EpicEntity;
import main.models.entity.SubtaskEntity;
import main.models.entity.TaskEntity;

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

    public TaskEntity mapToTaskEntity(TaskDto taskDto){
        TaskEntity entity = new TaskEntity(
                taskDto.getId(),
                taskDto.getName(),
                taskDto.getDescription(),
                taskDto.getStatus()
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
