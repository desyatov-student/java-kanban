package main.utils;

import main.models.dto.TaskDto;
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
}
