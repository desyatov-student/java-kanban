package ru.praktikum.kanban.service.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.praktikum.kanban.dto.CreateEpicDto;
import ru.praktikum.kanban.dto.EpicDto;
import ru.praktikum.kanban.dto.SubtaskDto;
import ru.praktikum.kanban.dto.UpdateEpicDto;
import ru.praktikum.kanban.model.Epic;
import ru.praktikum.kanban.model.TaskStatus;
import ru.praktikum.kanban.util.StringUtils;
import ru.praktikum.kanban.util.TimeUtils;

import static ru.praktikum.kanban.constant.DelimiterConstants.DELIMITER_COMMA;

@Mapper(config = ErrorUnmappedMapperConfig.class)
public interface EpicMapper {

    @Mapping(target = "subtasks", source = "subtasks")
    EpicDto toDto(Epic epic, List<SubtaskDto> subtasks);

    @Mapping(target = "subtasks", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "startTime", ignore = true)
    @Mapping(target = "duration", ignore = true)
    void updateEntityFromDto(UpdateEpicDto dto, @MappingTarget Epic epic);

    default String toString(Epic epic) {
        return StringUtils.joining(DELIMITER_COMMA,
                epic.getId(),
                TaskType.EPIC,
                epic.getName(),
                epic.getDescription(),
                epic.getStatus(),
                "",
                epic.getStartTime() == null ? "" : epic.getStartTime(),
                epic.getDuration() == null ? "" : epic.getDuration().toMinutes()
        );
    }

    @Mapping(target = "subtasks", expression = "java(new ArrayList<Integer>())")
    @Mapping(target = "startTime", ignore = true)
    @Mapping(target = "duration", ignore = true)
    Epic toEntity(Integer id, CreateEpicDto dto);

    default Epic toEntity(String[] values) {
        return new Epic(
                Integer.parseInt(values[0]),
                values[2],
                values[3],
                TaskStatus.valueOf(values[4]),
                List.of(),
                TimeUtils.parseDateTime(values[6]).orElse(null),
                TimeUtils.parseDuration(values[7]).orElse(null)
        );
    }
}
