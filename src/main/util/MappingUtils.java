package main.util;

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

public class MappingUtils {

    public TaskDto mapToTaskDto(TaskEntity entity){
        TaskDto dto = new TaskDto(
                entity.getId(),
                entity.name,
                entity.description,
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

    public TaskEntity mapToTaskEntity(CreateTaskDto createTaskDto, int taskId){
        TaskEntity entity = new TaskEntity(
                taskId,
                createTaskDto.getName(),
                createTaskDto.getDescription(),
                createTaskDto.getStatus()
        );
        return entity;
    }

    public EpicDto mapToEpicDto(EpicEntity epicEntity, List<SubtaskDto> subtasks){
        EpicDto dto = new EpicDto(
                epicEntity.getId(),
                epicEntity.name,
                epicEntity.description,
                epicEntity.getStatus(),
                subtasks
        );
        return dto;
    }

    public EpicEntity mapToEpicEntity(CreateEpicDto createEpicDto, int epicId){
        EpicEntity entity = new EpicEntity(
                epicId,
                createEpicDto.getName(),
                createEpicDto.getDescription(),
                TaskStatus.NEW
        );
        return entity;
    }

    public EpicEntity updateEpicEntity(EpicEntity epicEntity, UpdateEpicDto updateEpicDto){
        epicEntity.name = updateEpicDto.getName();
        epicEntity.description = updateEpicDto.getDescription();
        return epicEntity;
    }

    public SubtaskDto mapToSubtaskDto(SubtaskEntity subtaskEntity){
        SubtaskDto dto = new SubtaskDto(
                subtaskEntity.getId(),
                subtaskEntity.name,
                subtaskEntity.description,
                subtaskEntity.getStatus()
        );
        return dto;
    }

    public SubtaskEntity updateSubtaskEntity(SubtaskEntity subtaskEntity, UpdateSubtaskDto updateSubtaskDto){
        subtaskEntity.name = updateSubtaskDto.getName();
        subtaskEntity.description = updateSubtaskDto.getDescription();
        subtaskEntity.status = updateSubtaskDto.getStatus();
        return subtaskEntity;
    }

    public SubtaskEntity mapToSubtaskEntity(CreateSubtaskDto createSubtaskDto, int subtaskId){
        SubtaskEntity entity = new SubtaskEntity(
                subtaskId,
                createSubtaskDto.getName(),
                createSubtaskDto.getDescription(),
                createSubtaskDto.getStatus()
        );
        return entity;
    }
}
