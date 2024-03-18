package ru.praktikum.kanban.service.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.praktikum.kanban.model.TaskStatus;
import ru.praktikum.kanban.model.TaskType;
import ru.praktikum.kanban.model.dto.create.CreateEpic;
import ru.praktikum.kanban.model.dto.response.EpicDto;
import ru.praktikum.kanban.model.dto.response.SubtaskDto;
import ru.praktikum.kanban.model.dto.update.UpdateEpic;
import ru.praktikum.kanban.model.entity.Epic;
import ru.praktikum.kanban.util.StringUtils;

import static ru.praktikum.kanban.constant.DelimiterConstants.DELIMITER_COMMA;

@Mapper(config = ErrorUnmappedMapperConfig.class)
public interface EpicMapper {

    @Mapping(target = "subtasks", source = "subtasks")
    EpicDto toDto(Epic epic, List<SubtaskDto> subtasks);

    @Mapping(target = "subtasks", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateEntityFromDto(UpdateEpic dto, @MappingTarget Epic epic);

    default String toString(Epic epic) {
        return StringUtils.joining(DELIMITER_COMMA,
                epic.getId(),
                TaskType.EPIC,
                epic.name,
                epic.description,
                epic.status,
                ""
        );
    }

    @Mapping(target = "subtasks", expression = "java(new ArrayList<Integer>())")
    Epic toEntity(int id, CreateEpic dto);

    default Epic toEntity(String[] values) {
        return new Epic(
                Integer.parseInt(values[0]),
                values[2],
                values[3],
                TaskStatus.valueOf(values[4]),
                List.of()
        );
    }
}
