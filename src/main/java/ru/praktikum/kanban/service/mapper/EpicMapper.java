package ru.praktikum.kanban.service.mapper;

import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.praktikum.kanban.dto.CreateEpicDto;
import ru.praktikum.kanban.dto.EpicDto;
import ru.praktikum.kanban.dto.SubtaskDto;
import ru.praktikum.kanban.dto.UpdateEpicDto;
import ru.praktikum.kanban.model.Epic;
import ru.praktikum.kanban.model.TaskStatus;
import ru.praktikum.kanban.util.StringUtils;
import ru.praktikum.kanban.util.TimeUtils;

import static ru.praktikum.kanban.constant.CsvConstants.INDEX_TASK_DESCRIPTION;
import static ru.praktikum.kanban.constant.CsvConstants.INDEX_TASK_DURATION;
import static ru.praktikum.kanban.constant.CsvConstants.INDEX_TASK_END_TIME;
import static ru.praktikum.kanban.constant.CsvConstants.INDEX_TASK_ID;
import static ru.praktikum.kanban.constant.CsvConstants.INDEX_TASK_NAME;
import static ru.praktikum.kanban.constant.CsvConstants.INDEX_TASK_START_TIME;
import static ru.praktikum.kanban.constant.CsvConstants.INDEX_TASK_STATUS;
import static ru.praktikum.kanban.constant.DelimiterConstants.DELIMITER_COMMA;

@Mapper(config = ErrorUnmappedMapperConfig.class)
public interface EpicMapper {

    @Mapping(target = "subtasks", source = "subtasks")
    EpicDto toDto(Epic epic, List<SubtaskDto> subtasks);

    @Mapping(target = "subtasks", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "startTime", ignore = true)
    @Mapping(target = "duration", ignore = true)
    @Mapping(target = "endTime", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
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
                epic.getDuration() == null ? "" : epic.getDuration().toMinutes(),
                epic.getEndTime() == null ? "" : epic.getEndTime()
        );
    }

    @Mapping(target = "status", expression = "java(TaskStatus.NEW)")
    @Mapping(target = "subtasks", expression = "java(new ArrayList<Integer>())")
    @Mapping(target = "startTime", ignore = true)
    @Mapping(target = "duration", ignore = true)
    @Mapping(target = "endTime", ignore = true)
    Epic toEntity(Integer id, CreateEpicDto dto);

    default Epic toEntity(String[] values) {
        return new Epic(
                Integer.parseInt(values[INDEX_TASK_ID]),
                values[INDEX_TASK_NAME],
                values[INDEX_TASK_DESCRIPTION],
                TaskStatus.valueOf(values[INDEX_TASK_STATUS]),
                List.of(),
                TimeUtils.parseDateTime(values[INDEX_TASK_START_TIME]).orElse(null),
                TimeUtils.parseDuration(values[INDEX_TASK_DURATION]).orElse(null),
                TimeUtils.parseDateTime(values[INDEX_TASK_END_TIME]).orElse(null)
        );
    }
}
