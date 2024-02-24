package ru.praktikum.kanban.model.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.praktikum.kanban.model.dto.create.CreateEpicDto;
import ru.praktikum.kanban.model.dto.response.EpicDto;
import ru.praktikum.kanban.model.dto.response.SubtaskDto;
import ru.praktikum.kanban.model.dto.update.UpdateEpicDto;
import ru.praktikum.kanban.model.entity.EpicEntity;

@Mapper(config = ErrorUnmappedMapperConfig.class)
public interface EpicMapper {

    @Mapping(target = "subtasks", source = "subtasks")
    EpicDto toDto(EpicEntity entity, List<SubtaskDto> subtasks);

    @Mapping(target = "subtasks", ignore = true)
    EpicEntity toEntity(int id, CreateEpicDto dto);

    @Mapping(target = "subtasks", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateEntityFromDto(UpdateEpicDto dto, @MappingTarget EpicEntity entity);

}
