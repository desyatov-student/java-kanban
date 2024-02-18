package ru.praktikum.kanban.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.praktikum.kanban.model.dto.create.CreateTaskDto;
import ru.praktikum.kanban.model.dto.response.TaskDto;
import ru.praktikum.kanban.model.dto.update.UpdateTaskDto;
import ru.praktikum.kanban.model.entity.TaskEntity;


@Mapper(config = ErrorUnmappedMapperConfig.class)
public interface TaskMapper {

    TaskDto toDto(TaskEntity entity);

    TaskEntity toEntity(int id, CreateTaskDto dto);

    void updateEntityFromDto(UpdateTaskDto dto, @MappingTarget TaskEntity entity);

}
