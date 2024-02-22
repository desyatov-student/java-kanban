package ru.praktikum.kanban.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.praktikum.kanban.model.dto.create.CreateSubtaskDto;
import ru.praktikum.kanban.model.dto.response.SubtaskDto;
import ru.praktikum.kanban.model.dto.update.UpdateSubtaskDto;
import ru.praktikum.kanban.model.entity.SubtaskEntity;

@Mapper(config = ErrorUnmappedMapperConfig.class)
public interface SubtaskMapper {

    SubtaskDto toDto(SubtaskEntity entity);

    SubtaskEntity toEntity(int id, CreateSubtaskDto dto);

    @Mapping(target = "epicId", ignore = true)
    void updateEntityFromDto(UpdateSubtaskDto dto, @MappingTarget SubtaskEntity entity);

}
