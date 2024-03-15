package ru.praktikum.kanban.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.praktikum.kanban.model.TaskStatus;
import ru.praktikum.kanban.model.TaskType;
import ru.praktikum.kanban.model.dto.create.CreateSubtaskDto;
import ru.praktikum.kanban.model.dto.response.SubtaskDto;
import ru.praktikum.kanban.model.dto.update.UpdateSubtaskDto;
import ru.praktikum.kanban.model.entity.SubtaskEntity;
import ru.praktikum.kanban.util.StringUtils;

import static ru.praktikum.kanban.constant.DelimiterConstants.DELIMITER_COMMA;

@Mapper(config = ErrorUnmappedMapperConfig.class)
public interface SubtaskMapper {

    SubtaskDto toDto(SubtaskEntity entity);

    @Mapping(target = "epicId", ignore = true)
    void updateEntityFromDto(UpdateSubtaskDto dto, @MappingTarget SubtaskEntity entity);

    default String toString(SubtaskEntity task) {
        return StringUtils.joining(DELIMITER_COMMA,
                task.getId(),
                TaskType.SUBTASK,
                task.name,
                task.description,
                task.status,
                task.getEpicId()
        );
    }

    SubtaskEntity toEntity(int id, CreateSubtaskDto dto);

    default SubtaskEntity toEntity(String[] values) {
        return new SubtaskEntity(
                Integer.parseInt(values[0]),
                values[2],
                values[3],
                TaskStatus.valueOf(values[4]),
                Integer.parseInt(values[5])
        );
    }
}
